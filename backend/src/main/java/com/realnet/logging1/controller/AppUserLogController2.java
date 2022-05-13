package com.realnet.logging1.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.logging1.service.LoggingService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/log2" )
public class AppUserLogController2 {
	private LoggingService loggingService;
	@Autowired
	public AppUserLogController2(LoggingService loggingService) {
		super();
		this.loggingService = loggingService;
	}
	@GetMapping("/downloadLog/{filename}")
	public ResponseEntity<?> sendFile(@PathVariable("filename")String filename,HttpServletRequest request) throws Exception{
	//	AppUser a = appUserServiceImpl.getByUserName(username).orElse(null);
				Resource resource = loggingService.loadFileAsResource(filename);
				if(resource!=null) {
					String contentType = null;
			        try {
			            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			        } catch (IOException ex) {
			            throw new Exception("Could not find log file for file: "+filename);
			        }
			        // Fallback to the default content type if type could not be determined
			        if(contentType == null) {
			            contentType = "application/octet-stream";
			        }
			        return ResponseEntity.ok()
			                .contentType(MediaType.parseMediaType(contentType))
			                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
			                .body(resource);
				}else {
					return new ResponseEntity<>("File could not be loaded",HttpStatus.BAD_REQUEST);
				}
		}
}
