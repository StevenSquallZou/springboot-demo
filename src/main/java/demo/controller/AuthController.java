package demo.controller;


import demo.dto.AuthRequest;
import demo.dto.AuthResponse;
import demo.dto.ErrorResponse;
import demo.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/authenticate")
@Validated
@Slf4j
public class AuthController {
    protected AuthenticationManager authenticationManager;
    protected UserDetailsService userDetailsService;
    protected JwtUtil jwtUtil;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Validated AuthRequest authRequest) {
        Authentication authentication;

        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authentication = authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            log.warn("authenticate -> Invalid username or password for username={}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password!"));
        } catch (LockedException e) {
            log.warn("authenticate -> User account is locked for username={}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User account is locked!"));
        } catch (Exception e) {
            log.error("authenticate -> Authentication failed for username={}", authRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Authentication failed!"));
        }

        Map<String, Object> autorityMap = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (CollectionUtils.isNotEmpty(authorities)) {
            Set<String> authoritySet = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            autorityMap = new HashMap<>();
            autorityMap.put("authorities", authoritySet);
        }
        String token = jwtUtil.createToken(authentication.getName(), autorityMap);

        return ResponseEntity.ok().body(new AuthResponse(token));
    }

}
