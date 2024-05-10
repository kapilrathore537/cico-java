package com.cico.service;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.cico.payload.ApiResponse;
import com.cico.payload.JwtResponse;
import com.cico.payload.QRResponse;
import com.google.zxing.WriterException;

public interface IQRService {

	public QRResponse generateQRCode() throws WriterException,IOException;
	
	public ResponseEntity<?> QRLogin(String qrKey,String token);
	
	public JwtResponse ClientLogin(String token);

	public ResponseEntity<?> getLinkedDeviceData(HttpHeaders headers);

	public ResponseEntity<?> removeDeviceFromWeb(HttpHeaders headers);

	public ResponseEntity<?> updateWebLoginStatus(String token, String os, String deviceType, String browser);

	public ResponseEntity<?> getLinkedDeviceDataByUuid(String key);
	
	public  void jobEnd(String qrKey, String message);
}
