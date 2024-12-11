package pe.idat.dsfb.dcn.library.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("RXdKSps6tFLqkWgdIKGs4EFo696TnYjn7mR+6s+dSHo=".getBytes());
    private static final String PREFIX_TOKEN = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String username = claims.getSubject();
        List<GrantedAuthority> authorities = ((List<?>) claims.get("authorities")).stream()
                .map(authority -> {
                    if (authority instanceof LinkedHashMap) {
                        return new SimpleGrantedAuthority((String) ((LinkedHashMap<?, ?>) authority).get("authority"));
                    } else {
                        return new SimpleGrantedAuthority((String) authority);
                    }
                })
                .collect(Collectors.toList());

        if (username != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
