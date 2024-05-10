package com.cico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.payload.AdminResponse;
import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;

public interface IAdminService {
	
	public JwtResponse adminLogin(String adminId, String password);

	public ApiResponse createAdmin(String adminName, String adminEmail, String password);

	public AdminResponse getAdmin(String adminId);

	public AdminResponse updateAdmin(Integer adminId, String adminName, String adminEmail, MultipartFile file);

	ApiResponse deleteAdmin(Integer adminId);

	List<AdminResponse> getAll();

	AdminResponse profileUpload(MultipartFile profileImage, Integer adminId);
}

