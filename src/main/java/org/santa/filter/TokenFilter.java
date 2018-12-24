package org.santa.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        if (authorizationHeader != null) {

            String authorization = authorizationHeader.replace("Bearer ", "");

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(null, authorization));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
