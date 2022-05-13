package com.realnet.actionbuilder.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Header;
import com.realnet.actionbuilder.entity.Rn_cff_ActionBuilder_Lines;

public interface Rn_Cff_ActionBuilder_Service {
	List<Rn_cff_ActionBuilder_Header> getAll();
	Page<Rn_cff_ActionBuilder_Header> getAll(Pageable p);
	Rn_cff_ActionBuilder_Header getById(int id);
	Rn_cff_ActionBuilder_Header save(Rn_cff_ActionBuilder_Header actionBuilderHeader);
	Rn_cff_ActionBuilder_Header updateById(int id, Rn_cff_ActionBuilder_Header actionBuilderHeader);
	boolean deleteById(int id);
	
	// ----- Lines
	//List<Rn_cff_ActionBuilder_Lines> getLinesByHeaderId(int headerId);
	Page<Rn_cff_ActionBuilder_Lines> getLinesByHeaderId(int headerId, Pageable p);
	Rn_cff_ActionBuilder_Lines getLineById(int id);
	Rn_cff_ActionBuilder_Lines saveLine(int headerId, Rn_cff_ActionBuilder_Lines actionBuilderLine);
	Rn_cff_ActionBuilder_Lines updateLineById(int id, Rn_cff_ActionBuilder_Lines actionBuilderLine);
	boolean deleteLineById(int id);
	
	// BUSINESS LOGIC
	List<Rn_cff_ActionBuilder_Lines> getLinesByHeaderIdAndOrderBySeq(int headerId);
	Rn_cff_ActionBuilder_Lines getLinesByHeaderIdAndSeq(int headerId, int seq);
	
	
}
