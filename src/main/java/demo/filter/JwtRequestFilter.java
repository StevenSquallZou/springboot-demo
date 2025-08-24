package demo.filter;


import demo.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    protected static final String BEARER_PREFIX = "Bearer ";
    protected JwtUtil jwtUtil;
    protected UserDetailsService userDetailsService;


    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        super();
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.debug("doFilterInternal -> SecurityContextHolder already contains authentication, skipping JWT processing");
            filterChain.doFilter(request, response);
            return;
        }

        String token = deriveToken(request);
//        log.debug("doFilterInternal -> Derived token: {}", token);
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            log.warn("doFilterInternal -> Invalid JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = getUserDetails(token);
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(request, userDetails);

        filterChain.doFilter(request, response);
    }


    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


    protected UserDetails getUserDetails(String token) {
        String username = jwtUtil.extractUsername(token);
        if (StringUtils.isBlank(username)) {
            log.warn("doFilterInternal -> Username not found in JWT token");
            return null;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            log.warn("doFilterInternal -> No user details found for username={}", username);
            return null;
        }

        return userDetails;
    }


    protected String deriveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
//        log.debug("doFilterInternal -> Authorization header: {}", authorizationHeader);
        if (StringUtils.isBlank(authorizationHeader)) {
            log.warn("doFilterInternal -> Missing Authorization header");
            return null;
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.warn("doFilterInternal -> No Bearer found, invalid Authorization header format");
            return null;
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

}
