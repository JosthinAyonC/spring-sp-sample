package sasf.base.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import sasf.base.security.securityServices.IUserDetails;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${value.environment.jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${value.environment.jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        IUserDetails userPrincipal = (IUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", userPrincipal.getAuthorities().stream().map(s -> s.toString()).toArray())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Firma JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("JWT token inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token no tiene soporte: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims vacios: {}", e.getMessage());
        }

        return false;
    }
}
