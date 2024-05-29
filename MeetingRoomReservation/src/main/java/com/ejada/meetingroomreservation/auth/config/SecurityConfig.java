package com.ejada.meetingroomreservation.auth.config;

import com.ejada.meetingroomreservation.auth.jwt.JwtSecurityConfigurer;
import com.ejada.meetingroomreservation.auth.jwt.JwtTokenUtil;
import com.ejada.meetingroomreservation.auth.service.AppUserDetailsService;
import com.ejada.meetingroomreservation.auth.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // to use annotations instead of anti-matchers
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AppUserDetailsService applicationUserService;
	@Autowired
	private AuthUtils authUtils;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUnAuthResponse jwtUnAuthResponse;
	public static String[] URLS = { "/Auth/login", "/Auth/logout", "/Auth/register", "/error", "/User/deleteUser",
			"/Country/addCountry", "/Office/addOffice", "/Room/addRoom", "/Permission/addPermission", "/Roles/addRole",
			"/User/addUser", "/RolePermissions/addRolePermission", "/Permission/addPermissions"

	};

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().contentSecurityPolicy("frame-ancestors *").and()
				.frameOptions().sameOrigin().and().cors().and().csrf().disable().exceptionHandling().and().httpBasic()
				.authenticationEntryPoint(jwtUnAuthResponse).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().antMatchers(URLS)
				.permitAll().anyRequest().authenticated().and()
				.apply(new JwtSecurityConfigurer(jwtTokenUtil, authUtils));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(applicationUserService);
		return provider;

	}
}