package com.realnet.icon.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.service.Rn_ProjectSetup_Service;
import com.realnet.icon.entity.FavouriteEntity;
import com.realnet.icon.service.FavouriteService;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value = "/api")
@Api(tags = { "Favourite" })
public class FavouriteController {

	@Autowired
	private AppUserServiceImpl userService;
	
	@Autowired
	private FavouriteService favService;
	
	@Autowired
	private Rn_ProjectSetup_Service projectSetupService;

//	@Autowired
//	private Rn_ModuleSetup_Service moduleSetupService;
	
	
//	    // SAVE
		@ApiOperation(value = "Add New Favourite")
		@PostMapping(value = "/addFavById",consumes = {"application/json"})
		public ResponseEntity<?> saveFavourite( @RequestBody FavouriteEntity favourite,HttpSession session1) throws IOException {
			AppUser loggedInUser = userService.getLoggedInUser();
			favourite.setCreatedBy(loggedInUser.getUserId());
			favourite.setUpdatedBy(loggedInUser.getUserId());
			favourite.setAccountId(loggedInUser.getUserId());
				Rn_Project_Setup project = projectSetupService.getById(favourite.getObjectId());
			//	Rn_Module_Setup module = moduleSetupService.getById(favourite.getObjectId());
			//	favourite.setObjectId(project.getId());
			    favourite.setProject(project);
			//	favourite.setModule(module);			
			
			// Favourite fav =
			 FavouriteEntity savedFav = favService.save(favourite);
			 System.out.println("save favourite id"+savedFav);
			
			if (savedFav == null) {
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.MODULE_SETUP_API_TITLE);
				error.setMessage(Constant.MODULE_NOT_CREATED);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
			session1.setAttribute("favouriteId",savedFav.getId());
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.MODULE_SETUP_API_TITLE);
			success.setMessage(Constant.MODULE_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		}

	
		// DELETE
		@ApiOperation(value = "Delete A Favourite", response = FavouriteEntity.class)
		@DeleteMapping("/removeFavById/{id}")
		public ResponseEntity<?> deleteFavourite(@PathVariable(value = "id") int id) {
			boolean deleted = favService.deleteById(id);
			if (deleted) {
				SuccessPojo successPojo = new SuccessPojo();
				Success success = new Success();
				success.setTitle(Constant.MODULE_SETUP_API_TITLE);
				success.setMessage(Constant.MODULE_DELETED_SUCCESSFULLY);
				successPojo.setSuccess(success);
				return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
			} else {
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.MODULE_SETUP_API_TITLE);
				error.setMessage(Constant.MODULE_NOT_DELETED);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
		}

		
		// GET BY ID
		@ApiOperation(value = "Get A favourite", response = FavouriteEntity.class)
		@GetMapping("/getFavById/{id}")
		public ResponseEntity<FavouriteEntity> getFavouriteDetails(@PathVariable(value = "id") int id) {
			FavouriteEntity fav = favService.getById(id);
			if (fav == null) {
				throw new ResourceNotFoundException("fav not found with id " + id);
			}
			return ResponseEntity.ok().body(fav);
		}

		
}
