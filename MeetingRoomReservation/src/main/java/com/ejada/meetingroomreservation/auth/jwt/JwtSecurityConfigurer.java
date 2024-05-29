package com.ejada.meetingroomreservation.auth.jwt;

import com.ejada.meetingroomreservation.auth.utils.AuthUtils;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    final private JwtTokenUtil jwtTokenUtil;

    private AuthUtils authUtils;

    public JwtSecurityConfigurer(JwtTokenUtil jwtTokenUtil, AuthUtils authUtils) {
        super();
        this.jwtTokenUtil = jwtTokenUtil;
        this.authUtils = authUtils;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtAuthFilter filter = new JwtAuthFilter(jwtTokenUtil,authUtils);
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
