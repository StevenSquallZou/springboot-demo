package demo.service.impl;


import demo.redis.UserRedisManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsServiceRedisImpl")
@Slf4j
public class UserDetailsServiceRedisImpl implements UserDetailsService {
    protected UserRedisManager userRedisManager;


    @Autowired
    public UserDetailsServiceRedisImpl(UserRedisManager userRedisManager) {
        this.userRedisManager = userRedisManager;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        demo.model.profile.User user = userRedisManager.getUser(username);
        if (user == null) {
            log.warn("loadUserByUsername -> No user found in Redis and DB for username={}", username);
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        User.UserBuilder userBuilder = User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getResourceSet().toArray(new String[0]));

        return userBuilder.build();
    }

}
