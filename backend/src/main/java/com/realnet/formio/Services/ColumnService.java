package com.realnet.formio.Services;

import java.util.List;

import com.realnet.formio.entity.Board;
import com.realnet.formio.entity.Column;

public interface ColumnService {
	
	public Column createColumn(Column column);
	
	public Column updateColumn(Column column);
	
	public Column getColumn(Long id);
	
	public List<Column> getAllColumns();
	
	public void deleteColumn(Long id);
	
	public List<Column> getColumnsofBoard(Board bourd);

}
