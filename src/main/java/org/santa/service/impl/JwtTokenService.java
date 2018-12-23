package org.santa.service.impl;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.santa.model.dtos.Token;
import org.santa.model.entities.User;
import org.santa.repository.UsersRepository;
import org.santa.service.TokenService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT implementation of {@link TokenService}.
 */
@Component
public class JwtTokenService implements TokenService {

    private final Map<String, Key> keys;
    private final UsersRepository usersRepository;

    public JwtTokenService(UsersRepository usersRepository)
    throws Exception {

        this.usersRepository = usersRepository;
        keys = getRSAKeys();
    }

    // TODO move this out into external configuration.
    private static Map<String, Key> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Key> keys = new HashMap<>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
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
                        .signWith(SignatureAlgorithm.RS512, keys.get("private"))
                        .compact());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User verifyToken(String token) {

        JwtParser parser = Jwts
                .parser()
                .setSigningKey(keys.get("public"));

        String user = parser.parseClaimsJws(token)
                .getBody()
                .getSubject();

        return usersRepository.getUserByUsername(user);
    }
}
