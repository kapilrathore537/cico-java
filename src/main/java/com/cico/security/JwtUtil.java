package com.cico.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("${secret}")
	private String secret;


private String generateToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(4*24*60*60*1000)))
				.setIssuer("CICO").signWith(SignatureAlgorithm.HS256, secret).compact();
	}
	
	
public String generateTokenForStudent(String studentId,String subject,String deviceId, String role) {
	Map<String, Object> claims=new HashMap<>();
	claims.put("Role", role);
	claims.put("StudentId", studentId);
	claims.put("deviceId", deviceId);
	return generateToken(claims,subject);
}
	public String generateTokenForAdmin(String adminId) {
		Map<String, Object> claims=new HashMap<>();
		claims.put("Role", "ADMIN");
		claims.put("adminId", adminId);
		return generateToken(claims,adminId);
	}

	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public  String getUsername(String token) {
		return getClaims(token).getSubject();
		
	}
	
	public Object getHeader(String token,String key) {
		return getClaims(token).get(key);
	}
	
	private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
	
	public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }
	
	public Boolean validateToken(String token,String userId) {
        final String username = getUsername(token);
        return (username.equals(userId) && !isTokenExpired(token));
    }
	
	public String getRole(String token) {
		return getClaims(token).get("Role").toString();
	}
}


