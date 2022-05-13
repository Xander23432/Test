package com.realnet.formio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.formio.entity.Board;
import com.realnet.formio.entity.Column;

public interface ColumnRepository extends JpaRepository<Column, Long> {
	
	public List<Column> getColumnByBoard(Board board);
}
