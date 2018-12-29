package org.santa.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.santa.model.dtos.Token;
import org.santa.model.entities.User;
import org.santa.repository.UsersRepository;
import org.santa.service.TokenService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

/**
 * JWT implementation of {@link TokenService}.
 */
@Component
public class JwtTokenService implements TokenService {

    private final UsersRepository usersRepository;
    private final SecretKey key;

    public JwtTokenService(UsersRepository usersRepository) {

        this.usersRepository = usersRepository;
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token issueToken(User user) {

        Date now = new Date();
        Date expiry = Date
                .from(LocalDateTime.now().plus(3600, ChronoUnit.SECONDS)
                        .toInstant(UTC));

        return new Token(
                Jwts
                        .builder()
                        .setSubject(user.getId())
                        .setIssuedAt(now)
                        .setExpiration(expiry)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User verifyToken(String token) {

        String userId = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return usersRepository.findById(userId).orElse(null);
    }
}
