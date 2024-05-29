package com.ejada.meetingroomreservation.auth.jwt;

import com.ejada.meetingroomreservation.auth.config.SecurityConfig;
import com.ejada.meetingroomreservation.auth.utils.AuthUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

	private Logger logger = LogManager.getLogger(JwtAuthFilter.class);

	JwtTokenUtil jwtTokenUtil;
	private AuthUtils authUtils;

	public JwtAuthFilter(JwtTokenUtil jwtTokenUtil, AuthUtils authUtils) {
		super();
		this.jwtTokenUtil = jwtTokenUtil;
		this.authUtils = authUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = jwtTokenUtil.extractJwtFromRequest(request);
		try {
			if (token != null && !token.isBlank()) {

				logger.info("Starting check token from " + request.getRemoteAddr() + "  , API : "
						+ request.getRequestURI());
				jwtTokenUtil.verifyJwtToken(token);
				Claims claims = jwtTokenUtil.getClaims(token);
				String username = claims.getSubject();
				List<SimpleGrantedAuthority> simpleGrantedAuthorities = authUtils.getUserAuthorities(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						null, simpleGrantedAuthorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (ExpiredJwtException ex) {
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
				// todo refresh token
			} else {
				logger.error(ex);
				request.setAttribute("exception", "auth.token.expired");
			}
			try {
				authUtils.deleteUserExpiredToken(ex.getClaims().getSubject());
			} catch (Exception e) {
				request.setAttribute("exception", ex.getMessage());
				logger.error(ex);
				throw new RuntimeException(e);
			}
		} catch (BadCredentialsException ex) {
			request.setAttribute("exception", ex.getMessage());
			logger.error(ex);
		} catch (JwtException ex) {
			request.setAttribute("exception", ex.getMessage());
			logger.error(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			request.setAttribute("exception", ex.getMessage());
			logger.error(ex);
			throw new RuntimeException(ex);
		}
		filterChain.doFilter(request, response);
	}
}
