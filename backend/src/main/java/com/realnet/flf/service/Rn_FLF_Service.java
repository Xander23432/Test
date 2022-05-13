package com.realnet.flf.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.flf.entity.Rn_FLF_Header;
import com.realnet.flf.entity.Rn_FLF_Line;

public interface Rn_FLF_Service {
	List<Rn_FLF_Header> getAll();
	Page<Rn_FLF_Header> getAll(Pageable p);
	Rn_FLF_Header getById(int id);
	Rn_FLF_Header save(Rn_FLF_Header rn_flf_header);
	Rn_FLF_Header updateById(int id, Rn_FLF_Header rn_flf_header);
	boolean deleteById(int id);
	
	// ----- Lines
	Page<Rn_FLF_Line> getLinesByHeaderId(int headerId, Pageable p);
	Rn_FLF_Line getLineById(int id);
	Rn_FLF_Line saveLine(int headerId, Rn_FLF_Line rn_flf_line);
	Rn_FLF_Line updateLineById(int id, Rn_FLF_Line rn_flf_line);
	boolean deleteLineById(int id);
	
}
