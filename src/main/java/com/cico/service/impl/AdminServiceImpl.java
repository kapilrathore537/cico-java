package com.cico.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.InvalidCredentialsException;
import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Admin;
import com.cico.payload.AdminResponse;
import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.repository.AdminRepository;
import com.cico.security.JwtUtil;
import com.cico.service.IAdminService;
import com.cico.util.AppConstants;

@Service
public class AdminServiceImpl implements IAdminService {

	@Autowired
	private AdminRepository repo;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private FileServiceImpl fileService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public JwtResponse adminLogin(String adminId, String password) {
		Optional<Admin> adminEmail = repo.findByAdminEmail(adminId);
		if (adminEmail.isPresent()) {
			if (encoder.matches(password, adminEmail.get().getPassword())) {
				String token = jwtUtil.generateTokenForAdmin(adminId);
				return new JwtResponse(token);
			} else {
				throw new InvalidCredentialsException(AppConstants.INVALID_CREDENTIALS);
			}
		} else {
			throw new InvalidCredentialsException(AppConstants.INVALID_CREDENTIALS);
		}
	}

	@Override
	public ApiResponse createAdmin(String adminName, String adminEmail, String password) {
		Admin admin = new Admin(adminName, adminEmail, encoder.encode(password));

		admin.setUuid(UUID.randomUUID().toString());
		Optional<Admin> findByAdminEmail = repo.findByAdminEmail(admin.getAdminEmail());
		if (findByAdminEmail.isPresent()) {
			throw new ResourceAlreadyExistException(AppConstants.DATA_ALREADY_EXIST);
		} else {
			admin.setProfilePic(AppConstants.DEFAULT_ADMIN_IMAGE);
			admin = repo.save(admin);
		}
		return new ApiResponse(Boolean.TRUE, AppConstants.CREATE_SUCCESS, HttpStatus.CREATED);
	}

	@Override
	public AdminResponse getAdmin(String adminId) {
		Optional<Admin> findById = repo.findByAdminEmail(adminId);
		if (!findById.isPresent()) {
			throw new ResourceNotFoundException(AppConstants.NO_DATA_FOUND);
		}
		Admin admin = findById.get();
		AdminResponse adminResponse = modelMapper.map(admin, AdminResponse.class);
		return adminResponse;
	}

	@Override
	public AdminResponse updateAdmin(Integer adminId, String adminName, String adminEmail, MultipartFile file) {
		Admin admin = new Admin();

		Optional<Admin> findById = repo.findById(adminId);
		admin = findById.get();
		if (!findById.isPresent()) {
			throw new ResourceNotFoundException(AppConstants.DATA_ALREADY_EXIST);
		}
		if (admin.getAdminName() != null)
			admin.setAdminName(admin.getAdminName());

		if (admin.getAdminEmail() != null)
			admin.setAdminEmail(admin.getAdminEmail());

		if (admin.getProfilePic() != null && !admin.getProfilePic().isEmpty()) {
			List<String> img = new ArrayList<>();
			img.add(admin.getProfilePic());
			// fileService.deleteImagesInFolder(img, AppConstants.PROFILE_PIC);
			String image = fileService.uploadFileInFolder(file, AppConstants.PROFILE_PIC);
			admin.setProfilePic(image);
		} else {
			admin.setProfilePic(admin.getProfilePic());
		}

		admin.setUuid(admin.getUuid());
		admin.setPassword(admin.getPassword());

		repo.save(admin);

		AdminResponse adminResponse = modelMapper.map(admin, AdminResponse.class);
		return adminResponse;
	}

	@Override
	public ApiResponse deleteAdmin(Integer adminId) {
		Optional<Admin> admin = repo.findById(adminId);
		if (!admin.isPresent()) {
			throw new ResourceNotFoundException(AppConstants.NO_DATA_FOUND);
		}
		repo.deleteById(adminId);
		return new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS, HttpStatus.OK);
	}

	@Override
	public List<AdminResponse> getAll() {
		List<Admin> admins = repo.findAll();
		List<AdminResponse> adminsResponse = new ArrayList<>();
		for (Admin admin : admins) {
			adminsResponse.add(modelMapper.map(admin, AdminResponse.class));
		}

		return adminsResponse;
	}

	@Override
	public AdminResponse profileUpload(MultipartFile profileImage, Integer adminId) {
		Admin admin = repo.findById(adminId).get();

		if (profileImage != null && !profileImage.isEmpty()) {
			String image = fileService.uploadFileInFolder(profileImage, AppConstants.PROFILE_PIC);
			admin.setProfilePic(image);
			admin = repo.save(admin);
		}

		AdminResponse map = modelMapper.map(admin, AdminResponse.class);
		return map;
	}

}
