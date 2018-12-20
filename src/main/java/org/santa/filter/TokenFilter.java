package org.santa.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
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

    private final RequestMatcher requestMatcher;
    private final AuthenticationManager authenticationManager;

    public TokenFilter(
            RequestMatcher requestMatcher,
            AuthenticationManager authenticationManager) {

        Assert.notNull(requestMatcher,
                "requestMatcher cannot be null");

        this.requestMatcher = requestMatcher;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Extracts Bearer token from request and attempts to authenticate the
     * user against local repository. If authentication is successful
     * authentication details are added to the {@link org.springframework.security.core.context.SecurityContext}
     *
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        if (!requiresAuthentication(httpServletRequest)) {

            filterChain.doFilter(httpServletRequest, httpServletResponse);

            return;
        }

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader == null) {

            throw new BadCredentialsException("Authorization header not found");
        }

        String authorization = authorizationHeader.replace("Bearer ", "");

        final Authentication token = new UsernamePasswordAuthenticationToken(authorization, authorization);

        Authentication authenticate = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * Uses {@link RequestMatcher} to decide if authentication log should be
     * applied to the request.
     *
     */
    private boolean requiresAuthentication(HttpServletRequest request) {

        return requestMatcher.matches(request);
    }
}
