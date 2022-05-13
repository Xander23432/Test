package com.realnet.formdrag.services;

import java.util.List;

import com.realnet.formdrag.entity.Rn_wf_lines_3;

public interface Rn_wf_lines_3Service {
	
	public Rn_wf_lines_3 addToDB(Rn_wf_lines_3 rn_wf_lines_3);
	
	public Rn_wf_lines_3 updateInDB(Rn_wf_lines_3 rn_wf_lines_3);
	
	public Rn_wf_lines_3 getOneFromDBById(Long id);
	
	public List<Rn_wf_lines_3> getAll();
	
	public void deleteOneFromDBById(Long id);

}
