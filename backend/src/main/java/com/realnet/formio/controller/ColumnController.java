package com.realnet.formio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.formio.Services.ColumnService;
import com.realnet.formio.entity.Board;
import com.realnet.formio.entity.Column;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/cols", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/columns"})
public class ColumnController {
	
	@Autowired
	private ColumnService columnService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody Column column){
		return ResponseEntity.ok(this.columnService.createColumn(column));
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Column column){
		return ResponseEntity.ok(this.columnService.updateColumn(column));
	}
	
	@GetMapping("/get-all")
	public List<Column> getAll(){
		return this.columnService.getAllColumns();
	}
	
	@GetMapping("/get-one/{cid}")
	public Column getOneCol(@PathVariable("cid") Long cid) {
		return this.columnService.getColumn(cid);
	}
	
	@DeleteMapping("/delete/{cid}")
	public void deleteCol(@PathVariable("cid") Long cid) {
		this.columnService.deleteColumn(cid);
	}
	
	@GetMapping("/board/{bid}")
	public List<Column> get(@PathVariable("bid") Long bid){
		Board board = new Board();
		board.setbId(bid);
		return this.columnService.getColumnsofBoard(board);
	}

}
