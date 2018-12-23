package org.santa.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provider token authentication logic.
 */
public class TokenFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public TokenFilter(
            AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }

    /**
     * Extracts Bearer token from request and attempts to authenticate the
     * user against local repository. If authentication is successful
     * authentication details are added to the {@link org.springframework.security.core.context.SecurityContext}
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader == null) {

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String authorization = authorizationHeader.replace("Bearer ", "");

        final Authentication token = new UsernamePasswordAuthenticationToken(authorization, authorization);

        Authentication authenticate = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
