package TravelMgmtSys.ToutWayz_11.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * This component contains methods for handling all JWT operations
 */
@Component
public class JwtTokenUtil implements Serializable {

    public String generateToken(String username, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String requestId = UUID.randomUUID().toString();
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() +  60 * 60 * 10000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("created_at",new Date(System.currentTimeMillis()))
                .withClaim("requestId",requestId)
                .sign(algorithm);

        //setting session
        request.getSession().setAttribute(requestId,token);
        return token;
    }

    public String getEmailFromToken(String token){
        return token;
    }
}
