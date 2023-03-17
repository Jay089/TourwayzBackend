package TravelMgmtSys.ToutWayz_11.Config;




import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import TravelMgmtSys.ToutWayz_11.exceptions.RestAuthenticationFailureHandler;

/**
 * This is a security configuration component
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig{

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    /**
     * This method configures the security layer for our application
     * .which API should be public
     * .what type of authentication we will use
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // configuring the security layer
        httpSecurity.csrf().disable()
                .authorizeHttpRequests().requestMatchers("/login", "/register","/users/","/tours/","/booking/","/tours/search/getFeatured","/tours/search/getTourBySearch","/tours/{id}","/tours/{id}/reviews","/booking/","/booking/all/{email}").permitAll()
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
