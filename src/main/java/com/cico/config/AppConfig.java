package com.cico.config;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class AppConfig {

	@Value("${cloudinary.cloudName}")
	private String cloudName;
	@Value("${cloudinary.apiKey}")
	private String apiKey;
	@Value("${cloudinary.apiSecretKey}")
	private String secretKey;
	

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Cloudinary cloudinary() {

		return new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", secretKey));
	}

	@Bean
    FirebaseMessaging userFirebaseMessaging() throws IOException {
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource("firebasefile.json").getInputStream());
		FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions,"user");
		return FirebaseMessaging.getInstance(app);
	}
	
	
}