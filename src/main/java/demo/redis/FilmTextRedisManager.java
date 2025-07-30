package demo.redis;


import demo.model.sakila.FilmText;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class FilmTextRedisManager implements CommandLineRunner, InitializingBean {
    public static final String FILM_TEXT_VALUE_KEY_PREFIX = "filmTextValue:";
    public static final String FILM_TEXT_HASH_KEY_PREFIX = "filmTextHash:";
    private static final String FILM_TEXT_QUERY_WITH_LIMIT = "SELECT * FROM sakila.film_text ORDER BY film_id LIMIT %d OFFSET %d";
    private static final String FILM_TEXT_QUERY = "SELECT * FROM sakila.film_text WHERE film_id = ?";

    @Value("${app.redis.loadBatchSize}")
    protected int loadBatchSize;
    @Value("${app.redis.expirationSeconds.filmText:${app.redis.expirationSeconds.default}}")
    protected int expirationSeconds;
    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public FilmTextRedisManager(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void run(String... args) {
        batchLoadFilmText(loadBatchSize);
    }


    public void batchLoadFilmText(int loadBatchSize) {
        log.info("batchLoadFilmText -> Starting batch load of film text data into Redis, loadBatchSize: {}", loadBatchSize);

        boolean hasMore = true;
        int offset = 0;

        while (hasMore) {
            String sql = String.format(FILM_TEXT_QUERY_WITH_LIMIT, loadBatchSize, offset);
            log.debug("batchLoadFilmText -> Executing SQL: {}", sql);
            List<FilmText> filmTextList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FilmText.class));

            if (CollectionUtils.isEmpty(filmTextList)) {
                log.debug("batchLoadFilmText -> No more film text data to load, exiting loop");
                break;
            } else if (filmTextList.size() < loadBatchSize) {
                log.info("batchLoadFilmText -> Loaded {} rows, which is less than loadBatchSize, indicating no more data", filmTextList.size());
                hasMore = false;
            }

            log.debug("batchLoadFilmText -> queried {} rows from database", filmTextList.size());

            int loadedCount = 0;
            for (FilmText filmText : filmTextList) {
                updateOpsForValue(filmText);

                updateOpsForHash(filmText);

                loadedCount++;
            }

            log.debug("batchLoadFilmText -> partially loaded {} rows from database", loadedCount);

            offset += loadedCount;
        }

        log.info("batchLoadFilmText -> totally loaded {} rows into Redis", offset);
    }


    public FilmText getFilmText(Integer filmId) {
        String valueKey = getFilmTextValueKey(filmId);
        log.info("getFilmTextByFilmId -> retrieving film text for filmId: {}, redis valueKey: {}", filmId, valueKey);

        return (FilmText) redisTemplate.opsForValue().get(valueKey);
    }


    public Object getFilmTextAttribute(Integer filmId, String attributeName) {
        String hashKey = getFilmTextHashKey(filmId);

        return redisTemplate.opsForHash().get(hashKey, attributeName);
    }


    public void updateAll(FilmText filmText) {
        updateOpsForValue(filmText);

        updateOpsForHash(filmText);
    }


    private void updateOpsForValue(FilmText filmText) {
        String valueKey = getFilmTextValueKey(filmText.getFilmId());

        redisTemplate.opsForValue().set(valueKey, filmText, expirationSeconds, TimeUnit.SECONDS);
    }


    private void updateOpsForHash(FilmText filmText) {
        Map<String, Object> filmTextMap = new HashMap<>();
        filmTextMap.put("film_id", filmText.getFilmId());
        filmTextMap.put("title", filmText.getTitle());
        filmTextMap.put("description", filmText.getDescription());

        String hashKey = getFilmTextHashKey(filmText.getFilmId());

        redisTemplate.opsForHash().putAll(hashKey, filmTextMap);
        redisTemplate.expire(hashKey, expirationSeconds, TimeUnit.SECONDS);
    }


    public FilmText reloadFromDB(Integer filmId) {
        log.info("reloadFromDB -> Reloading film text from DB for filmId: {}", filmId);
        log.debug("reloadFromDB -> SQL: {}", FILM_TEXT_QUERY);
        List<FilmText> filmTextList = jdbcTemplate.query(FILM_TEXT_QUERY, new BeanPropertyRowMapper<>(FilmText.class), filmId);

        if (CollectionUtils.isEmpty(filmTextList)) {
            log.warn("reloadFromDB -> No film text found in DB for filmId: {}", filmId);
            return null;
        }

        FilmText filmText = filmTextList.get(0);
        updateAll(filmText);

        return filmText;
    }


    public static String getFilmTextValueKey(Integer filmId) {
        return FILM_TEXT_VALUE_KEY_PREFIX + filmId;
    }


    public static String getFilmTextHashKey(Integer filmId) {
        return FILM_TEXT_HASH_KEY_PREFIX + filmId;
    }


    @Override
    public void afterPropertiesSet() {
        log.info("loadBatchSize: {}", loadBatchSize);
        log.info("expirationSeconds: {}", expirationSeconds);
    }

}
