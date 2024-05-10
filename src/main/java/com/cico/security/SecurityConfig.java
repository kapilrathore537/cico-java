package com.cico.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebMvc
public class SecurityConfig {

	@Autowired
	private UserDetailsService detailService;
	
	@Autowired
	private AuthenticationEntryPoint entryPoint;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private SecurityFilter filter;
	
	String apiPaths[] = {
			"/swagger-ui/**","/swagger-ui.html", "/swagger-resources/**","/v3/api-docs", "/v2/api-docs", "/webjars/**",
			"/student/**","/file/**","/leave/**","/admin/**","/job/**","/technologyStack/**","/assignment/**","/course/**",
			"/newsEvents/**","/qr/**","/resources/**","/socket/**","/queue/**","/batch/**","/fees/**","/subject/**","/chapter/**","/question/**",
			"/exam/**","/task/**","/discussionForm/**","/announcement/**","*"
	};
	
	@Bean	
	public AuthenticationManager manager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder);
		provider.setUserDetailsService(detailService);
		
		return provider;		
	}

	
	@Bean
	public SecurityFilterChain chain(HttpSecurity security) throws Exception {
		return security.csrf().disable().authorizeRequests()
				.antMatchers(apiPaths).permitAll()
				.anyRequest().authenticated()
				.and().exceptionHandling()
				.authenticationEntryPoint(entryPoint)
				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}





	
	
	
	


