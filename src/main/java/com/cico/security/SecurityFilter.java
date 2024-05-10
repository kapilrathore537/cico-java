package com.cico.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cico.service.impl.CustomUserDetailsServiceImpl;
import com.cico.util.AppConstants;


@Component
public class SecurityFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil util;
	
	@Autowired
	private CustomUserDetailsServiceImpl detailsServiceImpl;
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token=request.getHeader(AppConstants.AUTHORIZATION);
		
		if(token!=null) {
			String username = util.getUsername(token);
			if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user=detailsServiceImpl.DataLoadByUsername(username,util.getRole(token));
			   UsernamePasswordAuthenticationToken	authenticationToken=new UsernamePasswordAuthenticationToken(username, user.getPassword(),user.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);

	}

}


