package com.cico.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskQuestionRequest {
	 
	  private String question;
	  private String videoUrl;
	  private List<MultipartFile> questionImages;
	  

}
