package org.santa.service.impl;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.santa.model.dtos.Token;
import org.santa.model.entities.User;
import org.santa.repository.UsersRepository;
import org.santa.service.TokenService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

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

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.add(Calendar.HOUR, 24);
        Date expiry = calendar.getTime();

        return new Token(
                Jwts
                        .builder()
                        .setSubject(user.getUsername())
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

        JwtParser parser = Jwts
                .parser()
                .setSigningKey(key);

        String user = parser.parseClaimsJws(token)
                .getBody()
                .getSubject();

        return usersRepository.getUserByUsername(user);
    }
}
