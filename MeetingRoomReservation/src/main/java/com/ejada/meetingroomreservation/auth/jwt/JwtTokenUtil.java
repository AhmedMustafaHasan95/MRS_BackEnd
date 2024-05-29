package com.ejada.meetingroomreservation.auth.jwt;

import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class JwtTokenUtil {
	private Logger logger = LogManager.getLogger(JwtTokenUtil.class);
	@Autowired
	private JWTConfig jwtConfig;
	private static final long dayToMillis = 24 * 60 * 60 * 1000;

	public String createToken(String subject, Map<String, Object> payload) throws Exception {
		try {
			Date expiryDate = new Date(
					new Date().getTime() + dayToMillis * jwtConfig.getTokenExpirationAfterDays().longValue());
			JwtBuilder builder = Jwts.builder().subject(subject).issuedAt(new Date()).expiration(expiryDate);
			if (payload != null) {
				Set<String> keySet = payload.keySet();
				for (String key : keySet) {
					builder.claim(key, payload.get(key));
				}
			}
			return builder.signWith(jwtConfig.getSecretKey()).compact();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			throw ex;
		}
	}

	public void verifyJwtToken(String token) throws Exception {
		try {
			Jwts.parser().verifyWith(jwtConfig.getSecretKey()).build().parseSignedClaims(token);
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature due to :", ex.getMessage());
			throw ex;
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token due to :", ex.getMessage());
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			logger.error("JWT token is expired due to :", ex.getMessage());
			throw ex;
		} catch (UnsupportedJwtException ex) {
			logger.error("JWT token is unsupported due to :", ex.getMessage());
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty due to :", ex.getMessage());
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parser().verifyWith(jwtConfig.getSecretKey()).build().parseSignedClaims(token).getPayload();
	}

	public String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtConfig.getAuthorizationHeader());
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtConfig.getTokenPrefix())) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
