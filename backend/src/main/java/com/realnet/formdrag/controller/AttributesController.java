//package com.realnet.formdrag.controller;
//
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
//import com.realnet.formdrag.entity.Attributes;
//import com.realnet.formdrag.entity.ModelEntity;
//import com.realnet.formdrag.services.AttributesService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
////@CrossOrigin("*")
//@RequestMapping(value = "/attributes", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//@Api(tags = {"/attributes"})
//public class AttributesController {
//
//	@Autowired
//	private AttributesService attributesService;
//	
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@RequestBody Attributes attributes){
//		return ResponseEntity.ok(this.attributesService.createAttributes(attributes));
//	}
//	
//	@PutMapping("/update")
//	public ResponseEntity<?> update(@RequestBody Attributes attributes){
//		return ResponseEntity.ok(this.attributesService.updateAttributes(attributes));
//	}
//	
//	@GetMapping("/get-all")
//	public List<Attributes> getAll(){
//		return this.attributesService.getAllAttributes();
//	}
//	
//	@GetMapping("/get-one/{aId}")
//	public Attributes getOne(@PathVariable("aId") Long aId) {
//		return this.attributesService.getAttribute(aId);
//	}
//	
//	@DeleteMapping("/delete/{aId}")
//	public void delete(@PathVariable("aId") Long aId) {
//		this.attributesService.deleteAttribute(aId);
//	}
//	
////	@GetMapping("/model/{mId}")
////	public List<?> getAttributesOfModel(@PathVariable("mId") Long mId){
////		ModelEntity model = new ModelEntity();
////		model.setmId(mId);
////		return this.attributesService.getAttributesByModel(model);
////	}
//	
//}
