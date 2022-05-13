package com.realnet.file_upload_dir.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.realnet.file_upload_dir.entity.FileDetails;
import com.realnet.file_upload_dir.helper.FileUploadHelper2;
import com.realnet.file_upload_dir.service.FileService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_Menu_Group" })
@CrossOrigin("*")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileUploadHelper2 fileUploadHelper;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image){
//		System.out.println(image.getOriginalFilename());
//		System.out.println(image.getContentType());
//		System.out.println(image.getSize());
//		System.out.println(image.getName());
		
		FileDetails file = new FileDetails();
		file.setFile_name(image.getOriginalFilename());
		file.setFile_type(image.getContentType());
		file.setFile_size(String.valueOf(image.getSize()));
		
		try {
			
			// check whether image is selected or not
			if(image.isEmpty()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Image should be selected.");
			}
			
			// checking the type of image
//			if(!image.getContentType().equals("image/jpeg")) {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//						.body("Only JPG images are allow.");
//			}
			
			// image upload
			boolean f = this.fileUploadHelper.uploadFile(image);
			if(f) {
//				return ResponseEntity.ok("Image uploaded successfully..");
//				return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(image.getOriginalFilename()).toUriString());
				FileDetails addToDb = this.fileService.addToDb(file);
				return ResponseEntity.ok("File Uploaded Successfully...");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Something went wrong...");
	}
}
