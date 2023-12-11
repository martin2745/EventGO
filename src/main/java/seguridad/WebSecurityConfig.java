package seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import autenticacion.UserDetailsServiceImpl;
import jwt.FiltroAutenticacionJWT;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Passwordencoder a usar
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FiltroAutenticacionJWT filtroAutenticacionJWT() {
        // Filtro de autenticación de peticiones basado en JWT
        return new FiltroAutenticacionJWT();
    }

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {

        http.userDetailsService(userDetailsService());

        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(final HttpServletRequest request) {
                return new CorsConfiguration().applyPermitDefaultValues();
            }
        });
        http.csrf().disable()
                .addFilterAfter(filtroAutenticacionJWT(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/media/**").permitAll()
                .antMatchers("/api/abierto/**").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }

}

