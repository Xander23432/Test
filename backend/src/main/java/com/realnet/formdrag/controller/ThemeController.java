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
//import com.realnet.formdrag.entity.Theme;
//import com.realnet.formdrag.services.ThemeService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
////@CrossOrigin("*")
//@RequestMapping(value = "/theme", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
//@Api(tags = {"/theme"})
//public class ThemeController {
//	
//	@Autowired
//	private ThemeService themeService;
//	
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@RequestBody Theme theme){
//		return ResponseEntity.ok(this.themeService.createTheme(theme));
//	}
//	
//	@PutMapping("/update")
//	public ResponseEntity<?> update(@RequestBody Theme theme){
//		return ResponseEntity.ok(this.themeService.updateTheme(theme));
//	}
//	
//	@GetMapping("/get-all")
//	public List<Theme> getAll(){
//		return this.themeService.getAllThemes();
//	}
//	
//	@GetMapping("/get-one/{tId}")
//	public Theme getOne(@PathVariable("tId") Long tId) {
//		return this.themeService.getTheme(tId);
//	}
//	
//	@DeleteMapping("/delete/{tId}")
//	public void delete(@PathVariable("tId") Long tId) {
//		this.themeService.deleteTheme(tId);
//	}
//
//}
