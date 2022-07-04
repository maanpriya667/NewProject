package com.test.demo.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthFilter extends BasicAuthenticationFilter {

    private transient final Environment environment;

    public AuthFilter(final AuthenticationManager authenticationManager, final Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws IOException, ServletException {

        final String authHeader = request.getHeader(environment.getProperty("auth.token.header.name"));

        final String property = environment.getProperty("auth.token.header.name.prefix");
        if (authHeader == null || property == null || !authHeader.startsWith(property)) {
            chain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        final String authHeader = request.getHeader(environment.getProperty("auth.token.header.name"));
        final String property = environment.getProperty("auth.token.header.name.prefix");
        if (authHeader != null && property != null) {
            final String token = authHeader.replace(property, "");

            final Claims claim = Jwts.parser().setSigningKey(environment.getProperty("auth.token.secret"))
                    .parseClaimsJws(token).getBody();

            List<String> permissions = new ArrayList<>();
            if (claim.get("permissions") != null) {
                permissions = (List<String>) claim.get("permissions");
            }

            final List<GrantedAuthority> authorities = new ArrayList<>();
            for (final String permission : permissions) {
                authorities.add(createGrantedAuthority(permission));
            }

            if (claim.getSubject() != null) {
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(claim.getSubject(), null,
                        authorities);
            }
        }

        return usernamePasswordAuthenticationToken;
    }


    private SimpleGrantedAuthority createGrantedAuthority(final String permission) {
        return new SimpleGrantedAuthority(permission);
    }

}
