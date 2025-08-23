package demo.service.impl;


import demo.redis.UserRedisManager;
import demo.service.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service("userServiceRedisImpl")
@Slf4j
public class UserServiceRedisImpl implements IUserService {
    protected UserRedisManager userRedisManager;


    @Autowired
    public UserServiceRedisImpl(UserRedisManager userRedisManager) {
        this.userRedisManager = userRedisManager;
    }


    @Override
    public UserDetails getUserDetailsByUsername(String username) {
        demo.model.profile.User user = userRedisManager.getUser(username);
        if (user == null) {
            log.warn("getUserDetailsByUsername -> No user found in Redis and DB for username={}", username);
            return null;
        }

        User.UserBuilder userBuilder = User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getResourceSet().toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false);

        return userBuilder.build();
    }

}
