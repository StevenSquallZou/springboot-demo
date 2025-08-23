package demo.service.api;


import org.springframework.security.core.userdetails.UserDetails;


public interface IUserService {
    UserDetails getUserDetailsByUsername(String username);

}
