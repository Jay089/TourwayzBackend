package TravelMgmtSys.ToutWayz_11.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    public AntPathRequestMatcher getMatcher(String pattern){
        return new AntPathRequestMatcher(pattern);
    }

    public List<AntPathRequestMatcher> excludeMatchers = new ArrayList<>(
            Arrays.asList(
//                    getMatcher("/tours/review"),
//                    getMatcher("/todos"),
                    getMatcher("/tours/search/getTourBySearch"),
                    getMatcher("/tours/search/getFeatured"),
                    getMatcher("/tours/"),
                    getMatcher("/register")
//"/tours/", "/tours/{id}","/tours/search/getTourBySearch","/tours/search/getFeatured","/auth/register","/auth/login"                    
            )
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Inside the doFilterInternal Method");
        if(request.getServletPath().equals("/login")){
            filterChain.doFilter(request,response);
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith(Helper.BEARER)){
                try{
                    logger.info("Verifying the token and passing the UsernamePasswordAuthenticationToken in context");
                    String token = authorizationHeader.substring(Helper.BEARER.length());
                    Algorithm algorithm = Algorithm.HMAC256(Helper.SECRET.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token); // verifying and decoding the token
                    String requestId = ((Claim) decodedJWT.getClaim("requestId")).asString();
                    String username = decodedJWT.getSubject();
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                    authenticationToken.setDetails(requestId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                    logger.info("Verifying token and adding to context is done");
                }catch(Exception exception){
                    logger.error("Error logging in {}",exception.getMessage());
                    response.setHeader("error",exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String,String> error = new HashMap<>();
                    error.put("error_message",exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        return excludeMatchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}

class Helper{
    public static final String BEARER = "Bearer ";
    public static final String SECRET = "secret";
}
