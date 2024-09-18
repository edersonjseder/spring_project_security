package com.javainfinite.security.filter;

import com.javainfinite.security.util.JwtTokenValidator;
import com.javainfinite.security.util.SecurityContants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtTokenValidationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("Request received for Jwt token validation");
        JwtTokenValidator validator = new JwtTokenValidator();
        validator.validateJwtToken(request, response, false);
        filterChain.doFilter(request, response);

    }
}
