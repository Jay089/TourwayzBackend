package TravelMgmtSys.ToutWayz_11.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                        AuthenticationException ex) throws IOException, ServletException
    {
        Integer httpServletResponseStatus = HttpServletResponse.SC_BAD_REQUEST;
        String message = "";
        String responseStatus = "BAD_REQUEST";
        if(ex.getMessage() == "Bad credentials") {
            message =  "Invalid Email Id / Password";
            responseStatus = "NOT_FOUND";
            httpServletResponseStatus = HttpServletResponse.SC_NOT_FOUND;
        } else {
            message =  ex.getMessage();
        }

        Map<String,String> response = new HashMap<>();
        response.put("message", message );
        response.put("httpStatus", responseStatus);
        response.put("timeStamp",String.valueOf(ZonedDateTime.now(ZoneId.of("Z"))));
        httpServletResponse.setStatus(httpServletResponseStatus);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
        out.flush();
    }
}
