package com.realnet.formio.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.realnet.formio.Services.CardService;
import com.realnet.formio.entity.Card;
import com.realnet.formio.entity.CardImage;
import com.realnet.formio.entity.Column;
//import com.realnet.formio.helper.FileUploadHelper;
import com.realnet.formio.repository.ImageRepository;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/cards", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/cards"})
public class CardController {
	
	@Autowired
	ImageRepository imageRepository;
	
//	@Autowired
//	private FileUploadHelper fileUploadHelper;
	
	@Autowired
	private CardService cardService;
	
//	headers=("content-type=multipart/*")
//	@RequestParam("image") MultipartFile file
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody Card card){
//		try {
//			
//			if(file.isEmpty()) {
//				System.out.println("Image is not selected..");
//			}
//			else {
//				card.setImage(file.getOriginalFilename());
//				
//				File saveFile = new ClassPathResource("/public/assets/images").getFile();
//				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
//				
////				java.time.LocalDate.now() 
//				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);	
//				
//				System.out.println("Image upload successfully..");
//			}	
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return ResponseEntity.ok(this.cardService.createCard(card));
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Card card){
		return ResponseEntity.ok(this.cardService.updateCard(card));
	}
	
	@GetMapping("/get-all")
	public List<Card> getAll(){
		return this.cardService.getAllCards();
	}
	
	@GetMapping("/get-one/{id}")
	public Card getOneCard(@PathVariable("id") Long id) {
		return this.cardService.getCard(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteCard(@PathVariable("id") Long id) {
		this.cardService.deleteCard(id);
	}
	
	@GetMapping("/column/{cid}")
	public List<Card> get(@PathVariable("cid") Long cid){
		Column column = new Column();
		column.setcId(cid);
		return this.cardService.getCardsOfColumn(column);
	}
	
	@PostMapping("/upload-image")
	public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image){
//		System.out.println(image.getOriginalFilename());
//		System.out.println(image.getContentType());
//		System.out.println(image.getSize());
//		System.out.println(image.getName());
		
		try {
			
			// check whether image is selected or not
			if(image.isEmpty()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Image should be selected.");
			}
			
			// checking the type of image
			if(!image.getContentType().equals("image/jpeg")) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Only JPG images are allow.");
			}
			
			// image upload
//			boolean f = this.fileUploadHelper.uploadFile(image);
//			if(f) {
////				return ResponseEntity.ok("Image uploaded successfully..");
//				return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(image.getOriginalFilename()).toUriString());
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Something went wrong...");
	}
	
	@PostMapping("/upload")
	public BodyBuilder uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		CardImage img = new CardImage(file.getOriginalFilename(), file.getContentType(),
				compressBytes(file.getBytes()));
		this.imageRepository.save(img);
		return ResponseEntity.status(HttpStatus.OK);
	}

	@GetMapping(path = { "/get/{imageName}" })
	public CardImage getImage(@PathVariable("imageName") String imageName) throws IOException {

		final Optional<CardImage> retrievedImage = imageRepository.findByName(imageName);
		CardImage img = new CardImage(retrievedImage.get().getName(), retrievedImage.get().getType(),
				decompressBytes(retrievedImage.get().getPicByte()));
		return img;
	}
	
	
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
