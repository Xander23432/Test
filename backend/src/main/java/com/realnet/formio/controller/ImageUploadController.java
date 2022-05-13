package com.realnet.formio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.formio.repository.ImageRepository;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/upload", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/image"})
public class ImageUploadController {
	
	@Autowired
	ImageRepository imageRepository;
	
	

}
