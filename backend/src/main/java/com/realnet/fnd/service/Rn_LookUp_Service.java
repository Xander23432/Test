package com.realnet.fnd.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.fnd.entity.Rn_Lookup_Values;

public interface Rn_LookUp_Service {
	List<Rn_Lookup_Values> getAll();

	Page<Rn_Lookup_Values> getAll(Pageable p);

	Rn_Lookup_Values getById(int id);

	Rn_Lookup_Values save(Rn_Lookup_Values project);

	Rn_Lookup_Values updateById(int id, Rn_Lookup_Values project);

	boolean deleteById(int id);

	// LOOKUP FIELDS (ATTRIBUTE, FLEX)
	List<String> getLookupValues();
	// TEXTFIELD, DROPDOWN ETC...
	List<String> getDataTypeValues();
	
	List<Rn_Lookup_Values> getExtensions();
	
	public void createTable(int id) throws IOException;
}
