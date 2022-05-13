package com.realnet.wfb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.wfb.entity.AddSectionButtonDTO;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_HeaderDTO;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.entity.Rn_Fb_Line_DTO;
import com.realnet.wfb.entity.Rn_WireFrameDTO;

public interface Rn_WireFrame_Service {
	// headers
	List<Rn_Fb_Header> getAll();

	Page<Rn_Fb_Header> getAll(Pageable p);

	Rn_Fb_Header getById(int id);

	Rn_Fb_Header save(Rn_Fb_Header Rn_Fb_Header);

	Rn_Fb_Header updateById(int id, Rn_Fb_Header rn_fb_header);

	boolean deleteById(int id);

	// headers for dropdown
	List<DropDownDTO> getFbHeadersForDropDown();

	List<DropDownDTO> getModuleWireframesForDropDown(int moduleId); // not tested yet
	// lines

	List<Rn_Fb_Line> getLinesByHeaderId(int headerId);

	Rn_Fb_Line getLineById(int id);

	Rn_Fb_Line saveLine(Rn_Fb_Line fb_line);

	Rn_Fb_Line updateLineById(int id, Rn_Fb_Line fb_line); // dummy

	// scripted method for wireframe default value
	boolean saveWireframe(Rn_WireFrameDTO wireframeDTO, String formType, int moduleId);
	// boolean editWireframe(int id, Rn_Fb_Header Rn_Fb_Header);

	/*
	 * if new section is added then it will re-entry all the line fields.
	 */
	boolean addNewSectionOrButton(int rn_fb_header_id, AddSectionButtonDTO addButtonSectionDTO);

	boolean addNewFieldInSection(int id, int sectionNum);

	// need modification
	boolean updateLine(int id, Rn_Fb_Line_DTO line);

	// Wireframe Builder Services
	List<Rn_Fb_Line> getButtonList(int header_id);

	List<Rn_Fb_Line> getExtraButton(int header_id);

	List<Rn_Fb_Line> getSection(int id);

	List<Rn_Fb_Line> getSectionFields(int section_number, int id);

	// used in angular front
	List<Rn_Fb_Line> getSectionFieldsForFrontEnd(int section_number, int header_id);

	List<Rn_Fb_Line> getPrimaryKeyField(int id);

	List<Rn_Fb_Line> getVarcharFields(int id);

	List<Rn_Fb_Line> getIntegerFields(int id);

	List<Rn_Fb_Line> getDateTimeFields(int id);

	List<Rn_Fb_Line> getLongtextFields(int id);

	List<Rn_Fb_Line> getDoubleFields(int id);

	// ====== for lines ========
	List<Rn_Fb_Line> getLineSectionFieldsForFrontEnd(int header_id); // used in angular front
	
	// add new field in lines part (used in only line, header line form)
	boolean addNewFieldInOnlyLineSection(int id, int sectionNumber);
	
	List<Rn_Fb_Line> getLinePrimarkKeyField(int id);

	List<Rn_Fb_Line> getLineSection(int id);

	List<Rn_Fb_Line> getLineVarcharFields(int id);

	List<Rn_Fb_Line> getLineIntegerFields(int id);

	// get only header fields (in OH, HL, OL, ML type form)
	List<Rn_Fb_Line> getHeaderFields(int id);

	// get only line fields (in OH, HL, OL, ML type form)
	List<Rn_Fb_Line> getLineFields(int id);

	// MULTI-LINE
	List<Rn_Fb_Line> getHidIntegerFields(int id);

	// delete section
	boolean deleteSection(int headerId, int sectionNumber);

	// delete single field (datatype field or button by id)
	boolean deleteLineById(int id);
	
	// ONLY LINE 
	List<Rn_Fb_Line> getOnlyLineSectionFields(int id, int sectionNumber);
	
	//ui_name change
	Rn_Fb_Header uinamechange(int id,Rn_Fb_HeaderDTO uiname);

}
