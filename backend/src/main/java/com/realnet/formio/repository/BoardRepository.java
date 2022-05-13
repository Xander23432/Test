package com.realnet.formio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.formio.entity.Board;
import com.realnet.formio.entity.Column;

public interface BoardRepository extends JpaRepository<Board, Long> {

//	public List<Column> getColumnsOfBoard(Column column);
}
