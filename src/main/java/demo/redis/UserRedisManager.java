package demo.redis;


import demo.exception.RedisLockException;
import demo.model.profile.User;
import demo.model.profile.UserResource;
import demo.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
@RefreshScope
@Slf4j
public class UserRedisManager implements CommandLineRunner, InitializingBean {
    public static final String USER_VALUE_KEY_PREFIX = "userValue:";
    private static final String LOCK_PREFIX = "lock:";
    private static final String USER_QUERY_ALL =
        """
        SELECT
            u.user_id, u.username, u.password_hash, r.resource_name
        FROM profile.user_resource ur
        JOIN profile.users u ON u.user_id = ur.user_id
        JOIN profile.resource r ON r.resource_id = ur.resource_id
        ORDER BY ur.user_resource_id, ur.user_id, ur.resource_id
        """;
    private static final String USER_QUERY =
        """
        SELECT
            u.user_id, u.username, u.password_hash, r.resource_name
        FROM profile.users u
        JOIN profile.user_resource ur ON ur.user_id = u.user_id
        JOIN profile.resource r ON r.resource_id = ur.resource_id
        WHERE u.username = ?
        """;

    @Value("${app.redis.expirationSeconds.user:${app.redis.expirationSeconds.default}}")
    protected int expirationSeconds;
    @Value("${app.redis.lock.seconds}")
    protected int lockSeconds;
    @Value("${app.redis.lock.max-retries}")
    protected int lockMaxRetries;
    @Value("${app.redis.lock.sleep-millis}")
    protected int lockSleepMillis;

    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String, Object> redisTemplate;
    protected Random random = new Random();


    @Autowired
    public UserRedisManager(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void run(String... args) {
        loadUsers();
    }


    public void loadUsers() {
        log.debug("loadUsers -> Executing SQL: \n{}", USER_QUERY_ALL);
        List<UserResource> userResourceList = jdbcTemplate.query(USER_QUERY_ALL, new BeanPropertyRowMapper<>(UserResource.class));
        if (CollectionUtils.isEmpty(userResourceList)) {
            log.debug("loadUsers -> No more userResource data to load, exiting");
            return;
        }

        log.debug("loadUsers -> queried {} userResource rows from database", userResourceList.size());

        Map<Integer, User> userMap = convertToUserMap(userResourceList);

        updateOpsForValues(userMap.values());
        log.info("loadUsers -> loaded {} users into Redis", userMap.size());
    }


    private Map<Integer, User> convertToUserMap(List<UserResource> userResourceList) {
        Map<Integer, User> userMap = new HashMap<>();

        for (UserResource userResource : userResourceList) {
            Integer userId = userResource.getUserId();

            User user = userMap.get(userId);
            if (user == null) {
                user = new User();
                user.setUserId(userResource.getUserId());
                user.setUsername(userResource.getUsername());
                user.setPasswordHash(userResource.getPasswordHash());

                userMap.put(userId, user);
            }

            user.addResource(userResource.getResourceName());
        }

        log.info("convertToUserMap -> converted to {} users", userMap.size());

        return userMap;
    }


    public User getUser(String username) {
        String valueKey = getUserValueKey(username);
        log.info("getUserByFilmId -> retrieving user for username: {}, redis valueKey: {}", username, valueKey);

        String lockKey = LOCK_PREFIX + valueKey;
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        int retries = 0;
        Boolean lockAcquired = false;

        try {
            while (retries < lockMaxRetries) {
                User user = (User) opsForValue.get(valueKey);
                if (user != null) {
                    log.info("getUserByUsername -> user found in Redis for username={}", username);
                    return user;
                }

                lockAcquired = opsForValue.setIfAbsent(lockKey, "locked", lockSeconds, TimeUnit.SECONDS);
                if (Boolean.TRUE.equals(lockAcquired)) {
                    return reloadFromDB(username);
                } else {
                    log.warn("getUserByUsername -> failed to acquire the lock for lockKey: {}, retries: {}", lockKey, retries);

                    MyUtils.silentlySleep(lockSleepMillis);
                }

                retries++;
            }
        } finally {
            if (Boolean.TRUE.equals(lockAcquired)) {
                redisTemplate.delete(lockKey);
            }
        }

        throw new RedisLockException("Failed to acquire the lock for lockKey" + lockKey + " after " + lockMaxRetries + " retries");
    }


    private void updateSingleUserCache(User user) {
        String valueKey = getUserValueKey(user.getUsername());

        redisTemplate.opsForValue().set(valueKey, user, getRandomTTl(), TimeUnit.SECONDS);
    }


    public List<Object> updateOpsForValues(Collection<User> users) {
        return redisTemplate.executePipelined(
            new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    if (CollectionUtils.isEmpty(users)) {
                        log.warn("updateOpsForValues -> empty user collection, nothing to update");
                        return null;
                    }

                    for (User user : users) {
                        String valueKey = getUserValueKey(user.getUsername());
                        operations.opsForValue().set(valueKey, user, getRandomTTl(), TimeUnit.SECONDS);
                    }

                    return null;
                }
            }
        );
    }


    public User reloadFromDB(String username) {
        log.info("reloadFromDB -> Reloading user from DB for username: {}", username);
        log.debug("reloadFromDB -> SQL: {}", USER_QUERY);
        List<UserResource> userResourceList = jdbcTemplate.query(USER_QUERY, new BeanPropertyRowMapper<>(UserResource.class), username);
        if (CollectionUtils.isEmpty(userResourceList)) {
            log.warn("reloadFromDB -> No userResource found in DB for username: {}", username);
            return null;
        }

        UserResource firstUserResource = userResourceList.get(0);
        User user = new User();
        user.setUserId(firstUserResource.getUserId());
        user.setUsername(firstUserResource.getUsername());
        user.setPasswordHash(firstUserResource.getPasswordHash());

        for (UserResource userResource : userResourceList) {
            user.addResource(userResource.getResourceName());
        }

        updateSingleUserCache(user);
        log.info("reloadFromDB -> updated cache for user: {}", user);

        return user;
    }


    public static String getUserValueKey(String username) {
        return USER_VALUE_KEY_PREFIX + username;
    }


    protected long getRandomTTl() {
        return expirationSeconds + random.nextLong(expirationSeconds);
    }


    @Override
    public void afterPropertiesSet() {
        log.info("expirationSeconds: {}", expirationSeconds);
        log.info("lockSeconds: {}", lockSeconds);
        log.info("lockMaxRetries: {}", lockMaxRetries);
        log.info("lockSleepMillis: {}", lockSleepMillis);
    }

}
