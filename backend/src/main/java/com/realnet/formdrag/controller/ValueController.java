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
//import com.realnet.formdrag.entity.Attributes;
//import com.realnet.formdrag.entity.Value;
//import com.realnet.formdrag.services.ValueService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
////@CrossOrigin("*")
//@RequestMapping(value = "/value", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//@Api(tags = {"/value"})
//public class ValueController {
//
//	@Autowired
//	private ValueService valueService;
//	
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@RequestBody Value value){
//		return ResponseEntity.ok(this.valueService.createValue(value));
//	}
//	
//	@PutMapping("/update")
//	public ResponseEntity<?> update(@RequestBody Value value){
//		return ResponseEntity.ok(this.valueService.updateValue(value));
//	}
//	
//	@GetMapping("/get-all")
//	public List<Value> getAll(){
//		return this.valueService.getAllValues();
//	}
//	
//	@GetMapping("/get-one/{vId}")
//	public Value getOne(@PathVariable("vId") Long vId) {
//		return this.valueService.getValue(vId);
//	}
//	
//	@DeleteMapping("/delete/{vId}")
//	public void delete(@PathVariable("vId") Long vId) {
//		this.valueService.deleteValue(vId);
//	}
//	
////	@GetMapping("/attribute/{aId}")
////	public List<Value> getByAttribute(@PathVariable("aId") Long aId){
////		Attributes att = new Attributes();
////		att.setaId(aId);
////		return this.valueService.getValueByAttribute(att);
////	}
//}
