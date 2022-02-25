package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {


    @Value("${forum.jwt.expiration}")
    private String timeToExpirate;

    @Value("${forum.jwt.secret}")
    private String secret;


    public String createToken(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Date now = new Date();

        return Jwts.builder()
                .setIssuer("Api Form")
                .setSubject(usuario.getId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ Long.parseLong(timeToExpirate)))
                .signWith( SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return  true;
        } catch(Exception ex) {
            return  false;
        }

    }

    public long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return  Long.parseLong(claims.getSubject());
    }
}
