package com.ejada.meetingroomreservation.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.Data;
import io.jsonwebtoken.io.Decoders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JWTConfig {
	private String secretKey;
	private String tokenPrefix;
	private Integer tokenExpirationAfterDays;

	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

}
