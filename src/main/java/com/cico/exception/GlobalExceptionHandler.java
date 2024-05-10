package com.cico.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cico.util.AppConstants;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<MyErrorResponse> showMYCustomError(ResourceNotFoundException rnfe)
	{
		return new ResponseEntity<MyErrorResponse>(
				new MyErrorResponse(new Date().toString(),
						"Resource Not Found",
						rnfe.getMessage()),HttpStatus.NOT_FOUND);
				
	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<MyErrorResponse> showMYCustomError(ResourceAlreadyExistException rfe)
	{
		return new ResponseEntity<MyErrorResponse>(
				new MyErrorResponse(new Date().toString(),
						"Resource Is Already Exist",
						rfe.getMessage()),HttpStatus.FOUND);
				
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<MyErrorResponse> showMYCustomError(InvalidCredentialsException ice)
	{
		return new ResponseEntity<MyErrorResponse>(
				new MyErrorResponse(new Date().toString(),
						"INVALID_CREDENTIALS",
						ice.getMessage()),HttpStatus.UNAUTHORIZED);			
	}
	
	@ExceptionHandler(UnauthorizeException.class)
	public ResponseEntity<MyErrorResponse> showMYCustomError(UnauthorizeException ue)
	{
		return new ResponseEntity<MyErrorResponse>(
				new MyErrorResponse(new Date().toString(),
						AppConstants.UNAUTHORIZED,
						ue.getMessage()),HttpStatus.UNAUTHORIZED);			
	}
	
}
