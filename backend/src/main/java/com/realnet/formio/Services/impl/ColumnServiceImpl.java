package com.realnet.formio.Services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.formio.Services.ColumnService;
import com.realnet.formio.entity.Board;
import com.realnet.formio.entity.Column;
import com.realnet.formio.repository.ColumnRepository;

@Service
public class ColumnServiceImpl implements ColumnService {
	
	@Autowired
	private ColumnRepository columnRepository;

	@Override
	public Column createColumn(Column column) {
		return this.columnRepository.save(column);
	}

	@Override
	public Column updateColumn(Column column) {
		return this.columnRepository.save(column);
	}

	@Override
	public Column getColumn(Long id) {
		return this.columnRepository.findById(id).get();
	}

	@Override
	public List<Column> getAllColumns() {
		return this.columnRepository.findAll();
	}

	@Override
	public void deleteColumn(Long id) {
		this.columnRepository.deleteById(id);
	}

	@Override
	public List<Column> getColumnsofBoard(Board bourd) {
		return this.columnRepository.getColumnByBoard(bourd);
	}

}
