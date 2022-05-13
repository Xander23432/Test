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

import com.realnet.fnd.response.CustomResponse;
import com.realnet.formio.Services.BoardService;
import com.realnet.formio.entity.Board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/api", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/boards"})
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	

	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody Board board){
		return ResponseEntity.ok(this.boardService.createBoard(board));
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody Board board){
		return ResponseEntity.ok(this.boardService.updateBoard(board));
	}
	
	
	@GetMapping("/get-all")
	public List<Board> getAll(){
		return this.boardService.getAllBoards();
	}
	
	@GetMapping("/get-one/{bid}")
	public Board getOne(@PathVariable("bid") Long bid) {
		return this.boardService.getBoard(bid);
	}
	
	@DeleteMapping("/delete-board/{bid}")
	public void delete(@PathVariable("bid") Long bid) {
		this.boardService.deleteBoard(bid);
	}
	
}
