package jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import autenticacion.UserDetailsServiceImpl;

public class FiltroAutenticacionJWT extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";


    @Autowired
    private UtilidadesJWT utilidadesJWT;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(FiltroAutenticacionJWT.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extraerToken(request);
            if (token != null && utilidadesJWT.validarToken(token)) {
                // Recuperar info. del usuario dueño del token y vincularla al contexto de la peticion
                String login = utilidadesJWT.extraerLogin(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(login);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Error en autenticación de usuario {}", e);
        }

        filterChain.doFilter(request, response);  // Deja continuar el procesameinto de la peticion con los siguientes filtros
    }

    private String extraerToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
            return headerAuth.substring(BEARER_PREFIX.length(), headerAuth.length());
        }

        return null;
    }
}
