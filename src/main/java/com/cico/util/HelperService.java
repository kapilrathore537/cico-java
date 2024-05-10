package com.cico.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class HelperService {
	
	public static long getMonthsDifference(LocalDate date1, LocalDate date2) {
        return ChronoUnit.MONTHS.between(date1, date2);
    }

	public String saveImage(MultipartFile imageFile, String destinationPath) {
		String currentDir = System.getProperty("user.dir")+destinationPath;
	    String originalFilename = StringUtils.cleanPath(imageFile.getOriginalFilename());
	    String imagePath = currentDir + originalFilename;
	    try {
	        File destinationFile = new File(imagePath);
	        FileUtils.forceMkdirParent(destinationFile);
	        imageFile.transferTo(destinationFile);
	        return destinationPath;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}

