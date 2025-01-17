package com.javainfinite.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JwtTokenCreator {

    Logger logger = LoggerFactory.getLogger(JwtTokenCreator.class);

    public void generateToken(HttpServletRequest request, HttpServletResponse response) {

        //Get the username from authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) { //verify whether user is authenticated
            String username = authentication.getName();
            SecretKey key = Keys.hmacShaKeyFor(SecurityContants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

            String jwt_token = Jwts.builder()
                    .setIssuer("javainfinite")
                    .setExpiration(new Date((new Date()).getTime() + 30000))
                    .setSubject("javainfinite_token")
                    .claim("username", username)
                    .claim("authorities", getStudentRoles((List<GrantedAuthority>) authentication.getAuthorities()))
                    .signWith(key)
                    .compact();

            if (request.getHeader(SecurityContants.REFRESH_HEADER) == null) {

                String refresh_token = Jwts.builder()
                        .setIssuer("javainfinite")
                        .setExpiration(new Date((new Date()).getTime() + 3000000))
                        .setSubject("javainfinite_token")
                        .claim("username", username)
                        .claim("authorities", getStudentRoles((List<GrantedAuthority>) authentication.getAuthorities()))
                        .signWith(key)
                        .compact();

                response.setHeader(SecurityContants.REFRESH_HEADER, refresh_token);
                logger.info("Refresh Token successfully generated: {}", refresh_token);
            }
            response.setHeader(SecurityContants.AUTHORIZATION_HEADER, jwt_token);
            logger.info("Token successfully generated: {}", jwt_token);

            Cookie jwtTokenCookie = new Cookie("token", jwt_token);

            jwtTokenCookie.setMaxAge(2 * 24 * 60 * 60);
            jwtTokenCookie.setHttpOnly(true);
            jwtTokenCookie.setSecure(false);
            jwtTokenCookie.setPath("/");

            response.addCookie(jwtTokenCookie);
        }
    }

    private String getStudentRoles(List<GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
