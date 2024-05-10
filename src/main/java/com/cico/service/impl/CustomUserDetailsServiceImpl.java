package com.cico.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cico.model.Admin;
import com.cico.model.Student;
import com.cico.repository.AdminRepository;
import com.cico.repository.StudentRepository;
import com.cico.util.Roles;


@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private StudentRepository studRepo;
	
	@Autowired
	private AdminRepository adminRepository;
	
	public UserDetails loadUserByUsername(String username) {
		Optional<Student> studOpt = studRepo.findByUserIdAndIsActive(username, true);
		if (studOpt.isEmpty())
			throw new UsernameNotFoundException("Username " + username + " Not Found");

		Student student = studOpt.get();
		List<GrantedAuthority> authority = List.of(new SimpleGrantedAuthority(Roles.STUDENT.toString()));
		return new User(username, student.getPassword(), authority);
	}
	
	public UserDetails loadUserByUsername1(String username) {
		Optional<Admin> adminOpt = adminRepository.findByAdminEmail(username);
		if (adminOpt.isEmpty())
			throw new UsernameNotFoundException("Username " + username + " Not Found");

		  Admin admin = adminOpt.get();
		List<GrantedAuthority> authority = List.of(new SimpleGrantedAuthority(Roles.ADMIN.toString()));
		return new User(username, admin.getPassword(), authority);
	}
	
	public UserDetails DataLoadByUsername(String username,String role) {
		if(role.equals(Roles.ADMIN.toString()))
			return	loadUserByUsername1(username);
		else
			return loadUserByUsername(username);
	}

}
