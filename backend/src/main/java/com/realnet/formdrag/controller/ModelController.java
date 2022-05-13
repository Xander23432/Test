//package com.realnet.formdrag.controller;

//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.realnet.formdrag.entity.ModelEntity;
//import com.realnet.formdrag.services.ModelService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
////@CrossOrigin("*")
//@RequestMapping(value = "/model", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//@Api(tags = {"/model"})
//public class ModelController {
//	
//	@Autowired
//	private ModelService modelService;
//	
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@RequestBody ModelEntity model){
//		return ResponseEntity.ok(this.modelService.createModel(model));
//	}
//	
//	@PutMapping("/update")
//	public ResponseEntity<?> update(@RequestBody ModelEntity model){
//		return ResponseEntity.ok(this.modelService.updateModel(model));
//	}
//	
//	@GetMapping("/get-all")
//	public List<ModelEntity> getAllModels(){
//		return this.modelService.getAllModel();
//	}
//	
//	@GetMapping("/get-one/{mId}")
//	public ModelEntity getOne(@PathVariable("mId") Long mId) {
//		return this.modelService.getModel(mId);
//	}
//	
//	@DeleteMapping("/delete/{mId}")
//	public void delete(@PathVariable("mId") Long mId) {
//		this.modelService.deleteModel(mId);
//	}
//
//}
