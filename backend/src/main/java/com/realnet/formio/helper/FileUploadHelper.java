//package com.realnet.formio.helper;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//@Component
//public class FileUploadHelper {
//
////	public final String UPLOAD_DIR="D:\\Installed Software\\Eclipse Workspace\\springboot\\springboot\\src\\main\\resources\\public\\assets\\images";
//	public final String UPLOAD_DIR= new ClassPathResource("/public/assets/images/").getFile().getAbsolutePath();
//
//	public FileUploadHelper() throws IOException {
//		
//	}
//	
//	public boolean uploadFile(MultipartFile multipartFile) {
//		boolean f = false;
//		
//		try {
//			
//			// one way of writing files
////			InputStream is = multipartFile.getInputStream();
////			byte data[] = new byte[is.available()];
////			is.read(data);
////			
////			// write in directory UPLOAD_DIR
////			FileOutputStream fos = new FileOutputStream(this.UPLOAD_DIR + File.separator + multipartFile.getOriginalFilename());
////			fos.write(data);
////			
////			fos.flush();
////			fos.close();
//			
//			// alternate way of file writing
//			
////			java.time.LocalDate.now() 
//			Files.copy(multipartFile.getInputStream(), Paths.get(this.UPLOAD_DIR + File.separator
//			+ multipartFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
//			
//			f = true;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return f;
//	}
//}
