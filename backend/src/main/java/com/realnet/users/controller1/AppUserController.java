package com.realnet.users.controller1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.config.EmailService;
import com.realnet.users.entity.PasswordResetRequest;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.entity1.AppUserDto;
import com.realnet.users.service1.AppUserServiceImpl;

@RestController
@RequestMapping(value = "/api")
public class AppUserController {
		private AppUserServiceImpl appUserServiceImpl;
		private EmailService emailService;
		@Autowired
		public AppUserController(AppUserServiceImpl appUserServiceImpl,
				EmailService emailService) {
			super();
			this.appUserServiceImpl = appUserServiceImpl;
			this.emailService = emailService;
		}
		@GetMapping("/getAllAppUser")
		public ResponseEntity<?> getAllUsers(			
				@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
				@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
			Pageable p = PageRequest.of(page,size);
			List<AppUser> u = appUserServiceImpl.getAllAppUsers(p);
			return new ResponseEntity<>(u,HttpStatus.OK);
		}
		@GetMapping("/getOneAppUser/{id}")
		public ResponseEntity<?> getOneAppUser(@PathVariable("id") Long id){
			Optional<AppUser> a = appUserServiceImpl.getOneUser(id);
			if(a.get()!=null) {
				return new ResponseEntity<>(a.get(),HttpStatus.OK);
			}
			return new ResponseEntity<>("User not found",HttpStatus.OK);
		}
		@PostMapping("/addOneAppUser")
		public ResponseEntity<?> addOneUser(@RequestBody AppUser appUser){
			AppUser a = appUserServiceImpl.addOneUser(appUser);
			return new ResponseEntity<>(a,HttpStatus.OK);
		}
		@PostMapping("/updateAppUser")
		public ResponseEntity<?> updateOneUser(@RequestBody AppUser appUser){
			AppUser a = appUserServiceImpl.updateOneUser(appUser);
			return new ResponseEntity<>(a,HttpStatus.OK);
		}
		@PostMapping("/updateAppUserDto")
		public ResponseEntity<?> updateAppUser(@RequestBody AppUserDto appUserDto){
			AppUser a = appUserServiceImpl.updateAppUserDto(appUserDto);
			if(a!=null) {
				return new ResponseEntity<>(a,HttpStatus.OK);
			}
			return new ResponseEntity<>("No user found",HttpStatus.BAD_REQUEST);
		}
		@PostMapping("/resetPasswordEmail/{userId}")
		public ResponseEntity<?> sendEmailForResetPassword(@PathVariable("userId") Long userId){
			AppUser a = appUserServiceImpl.getUserInfoByUserId(userId);
			//random string
			String hash = appUserServiceImpl.generateRandomHash(8);
			if(a!=null) {
				String email = a.getEmail();
				String subject = "Pass reset";
				String link = "http://"+hash+"/"+String.valueOf( a.getUserId());
				emailService.sendSimpleMessage(null, email, subject, link);
				return new ResponseEntity<>("Email sent success",HttpStatus.OK);
			}
			return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
		}
		
		@PostMapping("/resetPassword/{userId}/{hash}")
		public ResponseEntity<?> resetPassword(@PathVariable("userId") Long userId,@PathVariable("hash") String hash,@RequestBody PasswordResetRequest passwordResetRequest){
			
			AppUser a = appUserServiceImpl.resetPassword(userId,hash,passwordResetRequest.getNewPassword());
			if(a!=null) {
				return new ResponseEntity<>("Password Reset Successfull",HttpStatus.OK);
			}
			return new ResponseEntity<>("Password Not changed",HttpStatus.BAD_REQUEST);
		}
}
