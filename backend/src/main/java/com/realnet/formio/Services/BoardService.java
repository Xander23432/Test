package com.realnet.formio.Services;

import java.util.List;

import com.realnet.formio.entity.Board;

public interface BoardService {

	public Board createBoard(Board board);
	
	public Board updateBoard(Board board);
	
	public Board getBoard(Long id);
	
	public List<Board> getAllBoards();
	
	public void deleteBoard(Long id);
		
	
}
