package com.cico.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.QRResponse;
import com.cico.security.JwtUtil;
import com.cico.service.IQRService;
import com.cico.util.AppConstants;

@RestController
@RequestMapping("/qr")
@CrossOrigin("*")
public class QRController {

	@Autowired
	private IQRService qrService;
	
	@Autowired
	private JwtUtil util;
	
	@Autowired
	private IQRService service;
	
	
	@GetMapping("/qrGenerator")
	public ResponseEntity<QRResponse> generateQrCodeAsBase64() throws Exception{
		 QRResponse generateQRCode = qrService.generateQRCode();
      return ResponseEntity.ok(generateQRCode);
	}
	
	@PostMapping("/qrlogin/{qrKey}/{token}")
    public ResponseEntity<?> qrLoginWithToken(@PathVariable("qrKey") String qrKey,@PathVariable("token") String token){
        if(Objects.isNull(qrKey)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return qrService.QRLogin(qrKey, token);
    }

    @PostMapping("/updateWebLoginStatus")
    public ResponseEntity<?> updateWebLoginStatus(@RequestParam("token")String token,@RequestParam("os") String os,
			@RequestParam("deviceType") String deviceType, @RequestParam("browser") String browser){
    	return qrService.updateWebLoginStatus(token,os,deviceType,browser);

    }
    
    @GetMapping("/getLinkedDevice")
    public ResponseEntity<?> getLinkedDevice(@RequestHeader HttpHeaders headers){
    	return qrService.getLinkedDeviceData(headers);
    }
    
    @GetMapping("/getLinkedDeviceByUuid")
    public ResponseEntity<?> getLinkedDevice(@RequestParam("key") String key){
    	return qrService.getLinkedDeviceDataByUuid(key);
    }
    
    @DeleteMapping("/webLogout")
    public ResponseEntity<?> logoutUserFromWeb(@RequestHeader HttpHeaders headers){
    	System.out.println(headers.getFirst(AppConstants.AUTHORIZATION));
    	return qrService.removeDeviceFromWeb(headers);
    }
    
}
