package com.realnet.ncso.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.realnet.ncso.entity.Attachments;
import com.realnet.ncso.repository.AttachmentRepository;
import com.realnet.ncso.service.AttachmentService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso_attch", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso_attch"})
public class AttachmentController {
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody Attachments attachments){
		Attachments addToDb = this.attachmentService.addToDb(attachments);
		return ResponseEntity.ok(addToDb);
	}
	
	@PutMapping("/create")
	public ResponseEntity<?> update(@RequestBody Attachments attachments){
		Attachments addToDb = this.attachmentService.updateToDb(attachments);
		return ResponseEntity.ok(addToDb);
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		Attachments oneAttchById = this.attachmentService.getOneAttchById(id);
		return ResponseEntity.ok(oneAttchById);
	}
	
	@GetMapping("/get-all")
	public List<Attachments> getAll(){
		List<Attachments> allAttach = this.attachmentService.getAllAttach();
		return allAttach;
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		System.out.println("delete api");
		this.attachmentService.deleteAttchById(id);
	}
	
	@PostMapping("/upload")
	public BodyBuilder uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		Attachments file1 = new Attachments(file.getOriginalFilename(), file.getContentType(),
				compressBytes(file.getBytes()));
//		Attachments file1 = new Attachments();
//		file1.setFilename(file.getOriginalFilename());
//		file1.setType(file.getContentType());
//		file1.setPicByte(compressBytes(file.getBytes()));
		
		file1.setExternal(true);
		file1.setCancel(false);
		
		Attachments save = this.attachmentRepository.save(file1);
		return ResponseEntity.status(HttpStatus.OK);
//		return (BodyBuilder) save;
	}

//	@GetMapping(path = { "/get/{imageName}" })
//	public Attachments getImage(@PathVariable("imageName") String imageName) throws IOException {
//
//		final Optional<Attachments> retrievedImage = attachmentRepository.findByName(imageName);
//		Attachments img = new Attachments(retrievedImage.get().getFilename(), retrievedImage.get().getType(),
//				decompressBytes(retrievedImage.get().getPicByte()));
//		return img;
//	}
//	
	// compress the image bytes before storing it in the database
			public static byte[] compressBytes(byte[] data) {
				Deflater deflater = new Deflater();
				deflater.setInput(data);
				deflater.finish();

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
				byte[] buffer = new byte[1024];
				while (!deflater.finished()) {
					int count = deflater.deflate(buffer);
					outputStream.write(buffer, 0, count);
				}
				try {
					outputStream.close();
				} catch (IOException e) {
				}
				System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

				return outputStream.toByteArray();
			}

			// uncompress the image bytes before returning it to the angular application
			public static byte[] decompressBytes(byte[] data) {
				Inflater inflater = new Inflater();
				inflater.setInput(data);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
				byte[] buffer = new byte[1024];
				try {
					while (!inflater.finished()) {
						int count = inflater.inflate(buffer);
						outputStream.write(buffer, 0, count);
					}
					outputStream.close();
				} catch (IOException ioe) {
				} catch (DataFormatException e) {
				}
				return outputStream.toByteArray();
			}

}
