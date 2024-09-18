package com.javainfinite.security.filter;

import com.javainfinite.security.util.JwtTokenCreator;
import com.javainfinite.security.util.SecurityContants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;

public class JwtTokenCreatorFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtTokenCreatorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Token generator class");
        JwtTokenCreator creator = new JwtTokenCreator();
        creator.generateToken(request, response);
        filterChain.doFilter(request, response);
    }
}
