package com.cico.config;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.cico.model.Admin;
import com.cico.model.LeaveType;
import com.cico.repository.AdminRepository;
import com.cico.repository.LeaveTypeRepository;
import com.cico.util.Roles;

@Component
public class DataLoaderRunner implements CommandLineRunner{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private LeaveTypeRepository leaveTypeRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void run(String... args) throws Exception {
		
		List<Admin> findAll = adminRepository.findAll();
		if(findAll.isEmpty()) {
			Admin admin = new Admin();
			admin.setAdminEmail("cicoadmin@gmail.com");
			admin.setAdminName("CICO");
			admin.setProfilePic("https://res.cloudinary.com/df04kiqy3/image/upload/v1707472824/profilePic/DefaultUser_vg4fff.jpg");
			admin.setPassword(bCryptPasswordEncoder.encode("12345"));
			admin.setUuid(UUID.randomUUID().toString());
			admin.setRole(Roles.ADMIN.toString());
			adminRepository.save(admin);
		}
		
		
		List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
		if(leaveTypeList.isEmpty()) {
			List<LeaveType> asList = Arrays.asList(new LeaveType("Casual Leave"),
												   new LeaveType("Medical Leave"));
			leaveTypeRepository.saveAll(asList);
		}
		
	}


}
