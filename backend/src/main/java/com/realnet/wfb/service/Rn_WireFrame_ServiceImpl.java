package com.realnet.wfb.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.flf.service.Rn_Bcf_TechnologyStack_Service;
import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.service.Rn_ModuleSetup_Service;
import com.realnet.users.entity.User;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service.UserService;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;
import com.realnet.utils.WireFrameConstant;
import com.realnet.wfb.entity.AddSectionButtonDTO;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_HeaderDTO;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.entity.Rn_Fb_Line_DTO;
import com.realnet.wfb.entity.Rn_WireFrameDTO;
import com.realnet.wfb.repository.Rn_Fb_Header_Repository;
import com.realnet.wfb.repository.Rn_Fb_Line_Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_WireFrame_ServiceImpl implements Rn_WireFrame_Service {

//	@Value("${angularProjectPath}")
//	private String angularProjectPath;
//	@Value("${projectPath}")
//	private String projectPath;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Rn_Fb_Header_Repository fbHeaderRepository;

	@Autowired
	private Rn_Fb_Line_Repository fbLineRepository;

	@Autowired
	private AppUserServiceImpl userService;

	@Autowired
	private Rn_ModuleSetup_Service moduleService;

	@Autowired
	private Rn_Bcf_TechnologyStack_Service technologyService;

	@Override
	public List<Rn_Fb_Header> getAll() {
		return fbHeaderRepository.findAll();
	}

	@Override
	public Page<Rn_Fb_Header> getAll(Pageable page) {
		return fbHeaderRepository.findAll(page);
	}

	@Override
	public Rn_Fb_Header getById(int id) {
		Rn_Fb_Header fb_header = fbHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return fb_header;
	}

	@Override
	public Rn_Fb_Header save(Rn_Fb_Header fb_header) {
		Rn_Fb_Header savedHeader = fbHeaderRepository.save(fb_header);
		return savedHeader;
	}

	// ------- need modification ----------
	@Override
	public Rn_Fb_Header updateById(int id, Rn_Fb_Header headerRequest) {
		Rn_Fb_Header oldHeader = fbHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		
		AppUser loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
//		long accId = loggedInUser.getSys_account().getId();
		
		oldHeader.setUiName(headerRequest.getUiName());
		oldHeader.setTechStack(headerRequest.getTechStack());
		oldHeader.setObjectType(headerRequest.getObjectType());
		oldHeader.setSubObjectType(headerRequest.getSubObjectType());
		oldHeader.setFormType(headerRequest.getFormType());
		oldHeader.setFormCode(headerRequest.getFormCode());
		oldHeader.setTableName(headerRequest.getTableName());
		oldHeader.setLineTableName(headerRequest.getLineTableName());
		oldHeader.setMultilineTableName(headerRequest.getMultilineTableName());
		oldHeader.setJspName(headerRequest.getJspName());
		oldHeader.setControllerName(headerRequest.getControllerName());
		oldHeader.setServiceName(headerRequest.getServiceName());
		oldHeader.setServiceImplName(headerRequest.getServiceImplName());
		oldHeader.setDaoName(headerRequest.getDaoName());
		oldHeader.setDaoImplName(headerRequest.getDaoImplName());
		oldHeader.setBuild(headerRequest.isBuild());
		oldHeader.setUpdated(headerRequest.isUpdated());
		oldHeader.setMenuName(headerRequest.getMenuName());
		oldHeader.setConvertedTableName(headerRequest.getConvertedTableName());
		oldHeader.setHeaderName(headerRequest.getHeaderName());
		oldHeader.setUpdatedBy(userId);
//		oldHeader.setAccountId(accId);
		final Rn_Fb_Header updatedHeader = fbHeaderRepository.save(oldHeader);
		return updatedHeader;
	}

	@Override
	public boolean deleteById(int id) {
		if (!fbHeaderRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Fb_Header fb_header = fbHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		fbHeaderRepository.delete(fb_header);
		return true;
	}

	// ---- LINES PART ----
	@Override
	public List<Rn_Fb_Line> getLinesByHeaderId(int headerId) {
		return fbLineRepository.findLinesByHeaderId(headerId);
	}

	@Override
	public Rn_Fb_Line getLineById(int id) {
		Rn_Fb_Line fb_line = fbLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return fb_line;
	}

	@Override
	public Rn_Fb_Line saveLine(Rn_Fb_Line fb_line) {
		Rn_Fb_Line savedLine = fbLineRepository.save(fb_line);
		return savedLine;
	}

	// --------- need modification ----------
	@Override
	public Rn_Fb_Line updateLineById(int id, Rn_Fb_Line line) {
		Rn_Fb_Line oldLine = fbLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		oldLine.setFieldName(line.getFieldName());
		oldLine.setMapping(line.getMapping());
		oldLine.setDataType(line.getDataType());
		oldLine.setFormCode(line.getFormCode());
		oldLine.setKey1(line.getKey1());
		oldLine.setType1(line.getType1());
		oldLine.setMandatory(line.isMandatory());
		oldLine.setHidden(line.isHidden());
		oldLine.setReadonly(line.isReadonly());
		oldLine.setDependent(line.isDependent());
		oldLine.setDependent_on(line.getDependent_on());
		oldLine.setDependent_sp(line.getDependent_sp());
		oldLine.setDependent_sp_param(line.getDependent_sp_param());
		oldLine.setValidation_1(line.isValidation_1());
		oldLine.setVal_type(line.getVal_type());
		oldLine.setVal_sp(line.getVal_sp());
		oldLine.setVal_sp_param(line.getVal_sp_param());
		oldLine.setSequence(line.isSequence());
		oldLine.setSeq_name(line.getSeq_name());
		oldLine.setSeq_sp(line.getSeq_sp());
		oldLine.setSeq_sp_param(line.getSeq_sp_param());
		oldLine.setDefault_1(line.isDefault_1());
		oldLine.setDefault_type(line.getDefault_type());
		oldLine.setDefault_value(line.getDefault_value());
		oldLine.setDefault_sp(line.getDefault_sp());
		oldLine.setDefault_sp_param(line.getDefault_sp_param());
		oldLine.setCalculated_field(line.isCalculated_field());
		oldLine.setCal_sp(line.getCal_sp());
		oldLine.setCal_sp_param(line.getCal_sp_param());
		oldLine.setAdd_to_grid(line.getAdd_to_grid());
		oldLine.setSp_for_autocomplete(line.getSp_for_autocomplete());
		oldLine.setSp_for_dropdown(line.getSp_for_dropdown());
		oldLine.setSp_name_for_dropdown(line.getSp_name_for_dropdown());
		oldLine.setSp_name_for_autocomplete(line.getSp_name_for_autocomplete());
		oldLine.setType_field(line.getType_field());
		oldLine.setMethodName(line.getMethodName());
		oldLine.setSeq(line.getSeq());
		oldLine.setForm_type(line.getForm_type());
		oldLine.setSection_num(line.getSection_num());
		oldLine.setButton_num(line.getButton_num());
		oldLine.setType2(line.getType2());
		oldLine.setLine_table_name(line.getLine_table_name());
		oldLine.setLine_table_no(line.getLine_table_no());
		final Rn_Fb_Line updatedLine = fbLineRepository.save(oldLine);
		return updatedLine;
	}

	@Override
	public boolean deleteLineById(int id) {
		if (!fbLineRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Fb_Line fb_line = fbLineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		fbLineRepository.delete(fb_line);
		return true;
	}

	// default value
	@Override
	public boolean saveWireframe(Rn_WireFrameDTO wireframeDTO, String formType, int moduleId) {

		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
//		Long accountId = user.getSys_account().getId();

		String uiName = wireframeDTO.getUiName();
		// String formCode = wireframeDTO.getFormCode();
		String data = uiName.replaceAll("[-]+", "_");
		data = data.replace(" ", "");
		String formCode = data.concat("_view");
		// String techStack = wireframeDTO.getTechStack();
		// String menu_name = wireframeDTO.getMenuName();
//		String table_name = 
//		String form_code = request.getParameter("form_code");
//		String header_name = request.getParameter("header_name");
//		
//		String jsp_name = request.getParameter("jsp_name");
//		String controller_name = request.getParameter("controller_name");
//		String dao_name = request.getParameter("dao_name");
//		String dao_impl_name = request.getParameter("dao_impl_name");
//		String service_name = request.getParameter("service_name");
//		String service_impl_name = request.getParameter("service_impl_name");

		Rn_Module_Setup module = moduleService.getById(moduleId);
		log.debug("Module Details: {} ", module);
//		Rn_Project_Setup project = module.getProject();
//		int techStackId = project.getTechStackId();
//		Rn_Bcf_Technology_Stack technology = technologyService.getById(techStackId);
//		log.debug("Technology Details: {} ", technology);

		String techStack = module.getTechnologyStack();
		String objectType = wireframeDTO.getObjectType();
		String subObjectType = wireframeDTO.getSubObjectType();

		Rn_Fb_Header rn_fb_header = new Rn_Fb_Header();
		// rn_fb_header.setModule_id(module_id);
		rn_fb_header.setUiName(uiName);
		rn_fb_header.setTechStack(techStack);
		rn_fb_header.setObjectType(objectType);
		rn_fb_header.setSubObjectType(subObjectType);
		rn_fb_header.setFormType(formType);
		rn_fb_header.setHeaderName(uiName); // need mod
		rn_fb_header.setFormCode(formCode);
		rn_fb_header.setBuild(false);
		rn_fb_header.setUpdated(false);
		rn_fb_header.setModule(module);
		
		/* ALL JAVA Files Name */
		rn_fb_header.setControllerName(uiName+"Controller");
		
		
		/* table name depend on form type */
		if (WireFrameConstant.MULTILINE.equalsIgnoreCase(formType) ||
			WireFrameConstant.HEADER_LINE.equalsIgnoreCase(formType)) {
			rn_fb_header.setTableName(data.concat("_header"));
			rn_fb_header.setConvertedTableName(data.concat("_t_header"));
		} else if(WireFrameConstant.HEADER_ONLY.equalsIgnoreCase(formType)) {
			rn_fb_header.setTableName(data);
			rn_fb_header.setConvertedTableName(data.concat("_t"));
		} else if (WireFrameConstant.LINE_ONLY.equalsIgnoreCase(formType)) {
			rn_fb_header.setTableName(data.concat("_line"));
		}
		// objects name
		//rn_fb_header.setServiceName(serviceName);
		if (WireFrameConstant.MULTILINE.equalsIgnoreCase(formType) || WireFrameConstant.LINE_ONLY.equalsIgnoreCase(formType) || WireFrameConstant.HEADER_LINE.equalsIgnoreCase(formType)) {
			rn_fb_header.setLineTableName(data.concat("_t"));
			rn_fb_header.setMultilineTableName(data.concat("_t"));
		}
//		} else {
//			rn_fb_header.setLineTableName("");
//			rn_fb_header.setMultilineTableName("");
//		}
//		if (WireFrameConstant.MULTILINE.equalsIgnoreCase(formType)) {
//			rn_fb_header.setLineTableName("");
//			rn_fb_header.setMultilineTableName("");
//		}
		
		rn_fb_header.setCreatedBy(userId);
		rn_fb_header.setUpdatedBy(userId);
//		rn_fb_header.setAccountId(accountId);

		Rn_Fb_Header savedHeader = this.save(rn_fb_header);

		/* line table data save depends on form type */
		if (WireFrameConstant.HEADER_ONLY.equalsIgnoreCase(formType)) {
			/** Section code */
			Rn_Fb_Line rn_fb_line_section = new Rn_Fb_Line();
			rn_fb_line_section.setMapping(WireFrameConstant.SECTION + "1");
			rn_fb_line_section.setSeq(0);
			rn_fb_line_section.setFieldName(WireFrameConstant.SECTION + "1");
			rn_fb_line_section.setType_field(WireFrameConstant.SECTION);
			rn_fb_line_section.setSection_num(1);
			rn_fb_line_section.setButton_num(0);
			rn_fb_line_section.setForm_type(formType);
			rn_fb_line_section.setType1(formType);
			rn_fb_line_section.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_section.setDataType("N");
			rn_fb_line_section.setFormCode(formCode);
//				rn_fb_line_section.setKey1("");
			rn_fb_line_section.setType1(formType);
			rn_fb_line_section.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);
//				rn_fb_line_section.setDependent_on("");
//				rn_fb_line_section.setDependent_sp("");
//				rn_fb_line_section.setDependent_sp_param("");
			//rn_fb_line_section.setValidation_1("N");
			rn_fb_line_section.setValidation_1(false);
//				rn_fb_line_section.setVal_type("");
//				rn_fb_line_section.setVal_sp("");
//				rn_fb_line_section.setVal_sp_param("");
			rn_fb_line_section.setSequence(false);
//				rn_fb_line_section.setSeq_name("");
//				rn_fb_line_section.setSeq_sp("");
//				rn_fb_line_section.setSeq_sp_param("");
			rn_fb_line_section.setDefault_1(false);
//				rn_fb_line_section.setDefault_type("");
//				rn_fb_line_section.setDefault_value("");
//				rn_fb_line_section.setDefault_sp("");
//				rn_fb_line_section.setDefault_sp_param("");
			rn_fb_line_section.setCalculated_field(false);
//				rn_fb_line_section.setCal_sp("");
//				rn_fb_line_section.setCal_sp_param("");
			rn_fb_line_section.setAdd_to_grid(true);
			rn_fb_line_section.setSp_for_autocomplete(false);
			rn_fb_line_section.setSp_for_dropdown(false);
//				rn_fb_line_section.setSp_name_for_dropdown("");
//				rn_fb_line_section.setSp_name_for_autocomplete("");
//				rn_fb_line_section.setLine_table_name("");
			rn_fb_line_section.setLine_table_no(0);
			// WHO
			rn_fb_line_section.setCreatedBy(userId);
			rn_fb_line_section.setUpdatedBy(userId);
//			rn_fb_line_section.setAccountId(accountId);

			// HEADER_ID
			rn_fb_line_section.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_section);

			/** Primary Id */
			Rn_Fb_Line rn_fb_line_pk = new Rn_Fb_Line();
//			rn_fb_line2.setAccount_id(0);
			rn_fb_line_pk.setMapping("id");
			rn_fb_line_pk.setSeq(0);
			rn_fb_line_pk.setFieldName("select");
			rn_fb_line_pk.setType_field("id");
			rn_fb_line_pk.setSection_num(1);
			rn_fb_line_pk.setButton_num(0);
			rn_fb_line_pk.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_pk.setFormCode(formCode);
			rn_fb_line_pk.setKey1(WireFrameConstant.DT_PK);
			rn_fb_line_pk.setForm_type(formType);
			rn_fb_line_pk.setType1(formType);
			rn_fb_line_pk.setType2(WireFrameConstant.HEADER_TYPE);
//			rn_fb_line_pk.setMandatory("N");
//			rn_fb_line_pk.setHidden("N");
//			rn_fb_line_pk.setReadonly("N");
//			rn_fb_line_pk.setDependent("N");
			rn_fb_line_pk.setMandatory(false);
			rn_fb_line_pk.setHidden(false);
			rn_fb_line_pk.setReadonly(false);
			rn_fb_line_pk.setDependent(false);
//			rn_fb_line_pk.setDependent_on("");
//			rn_fb_line_pk.setDependent_sp("");
//			rn_fb_line_pk.setDependent_sp_param("");
			rn_fb_line_pk.setValidation_1(false);
//			rn_fb_line_pk.setVal_type("");
//			rn_fb_line_pk.setVal_sp("");
//			rn_fb_line_pk.setVal_sp_param("");
			rn_fb_line_pk.setSequence(false);
//			rn_fb_line_pk.setSeq_name("");
//			rn_fb_line_pk.setSeq_sp("");
//			rn_fb_line_pk.setSeq_sp_param("");
			rn_fb_line_pk.setDefault_1(false);
//			rn_fb_line_pk.setDefault_type("");
//			rn_fb_line_pk.setDefault_value("");
//			rn_fb_line_pk.setDefault_sp("");
//			rn_fb_line_pk.setDefault_sp_param("");
			rn_fb_line_pk.setCalculated_field(false);
//			rn_fb_line_pk.setCal_sp("");
//			rn_fb_line_pk.setCal_sp_param("");
//			rn_fb_line_pk.setAdd_to_grid("Y");
//			rn_fb_line_pk.setSp_for_autocomplete("N");
//			rn_fb_line_pk.setSp_for_dropdown("N");
			rn_fb_line_pk.setAdd_to_grid(true);
			rn_fb_line_pk.setSp_for_autocomplete(false);
			rn_fb_line_pk.setSp_for_dropdown(false);
//			rn_fb_line_pk.setSp_name_for_dropdown("");
//			rn_fb_line_pk.setSp_name_for_autocomplete("");
//			rn_fb_line_pk.setLine_table_name("");
			rn_fb_line_pk.setLine_table_no(0);
			// WHO
			rn_fb_line_pk.setCreatedBy(userId);
			rn_fb_line_pk.setUpdatedBy(userId);
//			rn_fb_line_pk.setAccountId(accountId);
			rn_fb_line_pk.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_pk);

			/**
			 * Header TextField - 4 default fields will add in db
			 */
			for (int i = 1; i <= 4; i++) {
				log.debug("i value : {}", i);
				Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();

//				rn_fb_line_textfield.setHeader_id(header_id);
//				rn_fb_line_textfield.setProject_id(project_id);
//				rn_fb_line_textfield.setModule_id(module_id);
//				rn_fb_line_textfield.setAccount_id(0);
				rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + i);
				rn_fb_line_textfield.setSeq(i);
				rn_fb_line_textfield.setFieldName(WireFrameConstant.LABEL + i);
				rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
				rn_fb_line_textfield.setSection_num(1);
				rn_fb_line_textfield.setButton_num(0);
				rn_fb_line_textfield.setForm_type(formType);
				rn_fb_line_textfield.setType1(formType);
				rn_fb_line_textfield.setType2(WireFrameConstant.HEADER_TYPE);
				rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
				rn_fb_line_textfield.setFormCode(formCode);
				rn_fb_line_textfield.setKey1("");
//				rn_fb_line_textfield.setMandatory("N");
//				rn_fb_line_textfield.setHidden("N");
//				rn_fb_line_textfield.setReadonly("N");
//				rn_fb_line_textfield.setDependent("N");
				rn_fb_line_textfield.setMandatory(false);
				rn_fb_line_textfield.setHidden(false);
				rn_fb_line_textfield.setReadonly(false);
				rn_fb_line_textfield.setDependent(false);
//				rn_fb_line_textfield.setDependent_on("");
//				rn_fb_line_textfield.setDependent_sp("");
//				rn_fb_line_textfield.setDependent_sp_param("");
				rn_fb_line_textfield.setValidation_1(false);
//				rn_fb_line_textfield.setVal_type("");
//				rn_fb_line_textfield.setVal_sp("");
//				rn_fb_line_textfield.setVal_sp_param("");
				rn_fb_line_textfield.setSequence(false);
//				rn_fb_line_textfield.setSeq_name("");
//				rn_fb_line_textfield.setSeq_sp("");
//				rn_fb_line_textfield.setSeq_sp_param("");
				rn_fb_line_textfield.setDefault_1(false);
//				rn_fb_line_textfield.setDefault_type("");
//				rn_fb_line_textfield.setDefault_value("");
//				rn_fb_line_textfield.setDefault_sp("");
//				rn_fb_line_textfield.setDefault_sp_param("");
				rn_fb_line_textfield.setCalculated_field(false);
//				rn_fb_line_textfield.setCal_sp("");
//				rn_fb_line_textfield.setCal_sp_param("");
//				rn_fb_line_textfield.setAdd_to_grid("Y");
//				rn_fb_line_textfield.setSp_for_autocomplete("N");
//				rn_fb_line_textfield.setSp_for_dropdown("N");
				rn_fb_line_textfield.setAdd_to_grid(true);
				rn_fb_line_textfield.setSp_for_autocomplete(false);
				rn_fb_line_textfield.setSp_for_dropdown(false);
//				rn_fb_line_textfield.setSp_name_for_dropdown("");
//				rn_fb_line_textfield.setSp_name_for_autocomplete("");
//				rn_fb_line_textfield.setLine_table_name("");
				rn_fb_line_textfield.setLine_table_no(0);

				// WHO
				rn_fb_line_textfield.setCreatedBy(userId);
				rn_fb_line_textfield.setUpdatedBy(userId);
//				rn_fb_line_textfield.setAccountId(accountId);

				rn_fb_line_textfield.setRn_fb_header(savedHeader);
				this.saveLine(rn_fb_line_textfield);
			}

			/** Header Button */
			Rn_Fb_Line rn_fb_line_button = new Rn_Fb_Line();

//			rn_fb_line_button.setHeader_id(header_id);
//			rn_fb_line_button.setProject_id(project_id);
//			rn_fb_line_button.setModule_id(module_id);
//			rn_fb_line_button.setAccount_id(0);
			rn_fb_line_button.setMapping("button1");
			rn_fb_line_button.setSeq(0);
			rn_fb_line_button.setFieldName("submit");
			rn_fb_line_button.setType_field(WireFrameConstant.BUTTON);
			rn_fb_line_button.setSection_num(0);
			rn_fb_line_button.setButton_num(1);
			rn_fb_line_button.setDataType("N");
			rn_fb_line_button.setFormCode(formCode);
			rn_fb_line_button.setKey1("");
			rn_fb_line_button.setForm_type(formType);
			rn_fb_line_button.setType1(formType);
			rn_fb_line_button.setType2(WireFrameConstant.HEADER_TYPE);

//			rn_fb_line_button.setMandatory("N");
//			rn_fb_line_button.setHidden("N");
//			rn_fb_line_button.setReadonly("N");
//			rn_fb_line_button.setDependent("N");

			rn_fb_line_button.setMandatory(false);
			rn_fb_line_button.setHidden(false);
			rn_fb_line_button.setReadonly(false);
			rn_fb_line_button.setDependent(false);

//			rn_fb_line_button.setDependent_on("");
//			rn_fb_line_button.setDependent_sp("");
//			rn_fb_line_button.setDependent_sp_param("");

			rn_fb_line_button.setValidation_1(false);
//			rn_fb_line_button.setVal_type("");
//			rn_fb_line_button.setVal_sp("");
//			rn_fb_line_button.setVal_sp_param("");
			rn_fb_line_button.setSequence(false);
//			rn_fb_line_button.setSeq_name("");
//			rn_fb_line_button.setSeq_sp("");
//			rn_fb_line_button.setSeq_sp_param("");
			rn_fb_line_button.setDefault_1(false);
//			rn_fb_line_button.setDefault_type("");
//			rn_fb_line_button.setDefault_value("");
//			rn_fb_line_button.setDefault_sp("");
//			rn_fb_line_button.setDefault_sp_param("");
			rn_fb_line_button.setCalculated_field(false);
//			rn_fb_line_button.setCal_sp("");
//			rn_fb_line_button.setCal_sp_param("");

//			rn_fb_line_button.setAdd_to_grid("Y");
//			rn_fb_line_button.setSp_for_autocomplete("N");
//			rn_fb_line_button.setSp_for_dropdown("N");
			rn_fb_line_button.setAdd_to_grid(true);
			rn_fb_line_button.setSp_for_autocomplete(false);
			rn_fb_line_button.setSp_for_dropdown(false);

//			rn_fb_line_button.setSp_name_for_dropdown("");
//			rn_fb_line_button.setSp_name_for_autocomplete("");
//			rn_fb_line_button.setLine_table_name("");
			rn_fb_line_button.setLine_table_no(0);

			// WHO
			rn_fb_line_button.setCreatedBy(userId);
			rn_fb_line_button.setUpdatedBy(userId);
//			rn_fb_line_button.setAccountId(accountId);

			rn_fb_line_button.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_button);
		} /** Only Header End */

		/** ONLY-LINE Default values */
		else if (WireFrameConstant.LINE_ONLY.equalsIgnoreCase(formType)) {

			/** Only Line Section field */
			Rn_Fb_Line rn_fb_line_section = new Rn_Fb_Line();
//			rn_fb_line_section.setHeader_id(header_id);
//			rn_fb_line_section.setProject_id(project_id);
//			rn_fb_line_section.setModule_id(module_id);
//			rn_fb_line_section.setAccount_id(0);
			rn_fb_line_section.setMapping(WireFrameConstant.SECTION);
			rn_fb_line_section.setSeq(0);
			rn_fb_line_section.setForm_type(formType);
			rn_fb_line_section.setFieldName(WireFrameConstant.SECTION);
			rn_fb_line_section.setType_field(WireFrameConstant.SECTION);
			rn_fb_line_section.setSection_num(1);
			rn_fb_line_section.setButton_num(0);
			rn_fb_line_section.setType1(formType);
			rn_fb_line_section.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_section.setDataType("N");
			rn_fb_line_section.setFormCode(formCode);
			rn_fb_line_section.setKey1("");

//			rn_fb_line_section.setMandatory("N");
//			rn_fb_line_section.setHidden("N");
//			rn_fb_line_section.setReadonly("N");
//			rn_fb_line_section.setDependent("N");
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);

//			rn_fb_line_section.setDependent_on("");
//			rn_fb_line_section.setDependent_sp("");
//			rn_fb_line_section.setDependent_sp_param("");
			rn_fb_line_section.setValidation_1(false);
//			rn_fb_line_section.setVal_type("");
//			rn_fb_line_section.setVal_sp("");
//			rn_fb_line_section.setVal_sp_param("");
			rn_fb_line_section.setSequence(false);
//			rn_fb_line_section.setSeq_name("");
//			rn_fb_line_section.setSeq_sp("");
//			rn_fb_line_section.setSeq_sp_param("");
			rn_fb_line_section.setDefault_1(false);
//			rn_fb_line_section.setDefault_type("");
//			rn_fb_line_section.setDefault_value("");
//			rn_fb_line_section.setDefault_sp("");
//			rn_fb_line_section.setDefault_sp_param("");
			rn_fb_line_section.setCalculated_field(false);
//			rn_fb_line_section.setCal_sp("");
//			rn_fb_line_section.setCal_sp_param("");
//			rn_fb_line_section.setAdd_to_grid("N");
//			rn_fb_line_section.setSp_for_autocomplete("N");
//			rn_fb_line_section.setSp_for_dropdown("N");
			rn_fb_line_section.setAdd_to_grid(false);
			rn_fb_line_section.setSp_for_autocomplete(false);
			rn_fb_line_section.setSp_for_dropdown(false);
//			rn_fb_line_section.setSp_name_for_dropdown("");
//			rn_fb_line_section.setSp_name_for_autocomplete("");
//			rn_fb_line_section.setLine_table_name("");
			rn_fb_line_section.setLine_table_no(0);

			// WHO
			rn_fb_line_section.setCreatedBy(userId);
			rn_fb_line_section.setUpdatedBy(userId);
//			rn_fb_line_section.setAccountId(accountId);

			rn_fb_line_section.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_section);

			/**
			 * Only Line Primary Id
			 */
			Rn_Fb_Line rn_fb_line_pk = new Rn_Fb_Line();
//			rn_fb_line_pk.setHeader_id(header_id);
//			rn_fb_line_pk.setProject_id(project_id);
//			rn_fb_line_pk.setModule_id(module_id);
//			rn_fb_line_pk.setAccount_id(0);
			rn_fb_line_pk.setMapping("id");
			rn_fb_line_pk.setSeq(0);
			rn_fb_line_pk.setForm_type(formType);
			rn_fb_line_pk.setFieldName("select");
			rn_fb_line_pk.setType_field("id");
			rn_fb_line_pk.setSection_num(1);
			rn_fb_line_pk.setButton_num(0);
			rn_fb_line_pk.setType1(formType);
			rn_fb_line_pk.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_pk.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_pk.setFormCode(formCode);
			rn_fb_line_pk.setKey1(WireFrameConstant.DT_PK);

//			rn_fb_line_pk.setMandatory("N");
//			rn_fb_line_pk.setHidden("N");
//			rn_fb_line_pk.setReadonly("N");
//			rn_fb_line_pk.setDependent("N");
			rn_fb_line_pk.setMandatory(false);
			rn_fb_line_pk.setHidden(false);
			rn_fb_line_pk.setReadonly(false);
			rn_fb_line_pk.setDependent(false);
//			rn_fb_line_pk.setDependent_on("");
//			rn_fb_line_pk.setDependent_sp("");
//			rn_fb_line_pk.setDependent_sp_param("");
			rn_fb_line_pk.setValidation_1(false);
//			rn_fb_line_pk.setVal_type("");
//			rn_fb_line_pk.setVal_sp("");
//			rn_fb_line_pk.setVal_sp_param("");
			rn_fb_line_pk.setSequence(false);
//			rn_fb_line_pk.setSeq_name("");
//			rn_fb_line_pk.setSeq_sp("");
//			rn_fb_line_pk.setSeq_sp_param("");
			rn_fb_line_pk.setDefault_1(false);
//			rn_fb_line_pk.setDefault_type("");
//			rn_fb_line_pk.setDefault_value("");
//			rn_fb_line_pk.setDefault_sp("");
//			rn_fb_line_pk.setDefault_sp_param("");
			rn_fb_line_pk.setCalculated_field(false);
//			rn_fb_line_pk.setCal_sp("");
//			rn_fb_line_pk.setCal_sp_param("");
//			rn_fb_line_pk.setAdd_to_grid("N");
//			rn_fb_line_pk.setSp_for_autocomplete("N");
//			rn_fb_line_pk.setSp_for_dropdown("N");
			rn_fb_line_pk.setAdd_to_grid(false);
			rn_fb_line_pk.setSp_for_autocomplete(false);
			rn_fb_line_pk.setSp_for_dropdown(false);
//			rn_fb_line_pk.setSp_name_for_dropdown("");
//			rn_fb_line_pk.setSp_name_for_autocomplete("");
//			rn_fb_line_pk.setLine_table_name("");
			rn_fb_line_pk.setLine_table_no(0);
			// WHO
			rn_fb_line_pk.setCreatedBy(userId);
			rn_fb_line_pk.setUpdatedBy(userId);
//			rn_fb_line_pk.setAccountId(accountId);
			rn_fb_line_pk.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_pk);

			/** ONLY-LINE TextField - 4 fields will save in db */
			for (int i = 1; i <= 4; i++) {
				Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();

//				rn_fb_line_textfield.setHeader_id(header_id);
//				rn_fb_line_textfield.setProject_id(project_id);
//				rn_fb_line_textfield.setModule_id(module_id);
//				rn_fb_line_textfield.setAccount_id(0);
				rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + i);
				rn_fb_line_textfield.setSeq(i);
				rn_fb_line_textfield.setForm_type(formType);
				rn_fb_line_textfield.setFieldName("column" + i);
				rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
				rn_fb_line_textfield.setSection_num(1);
				rn_fb_line_textfield.setButton_num(0);
				rn_fb_line_textfield.setType1(formType);
				rn_fb_line_textfield.setType2(WireFrameConstant.LINE_TYPE);
				rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
				rn_fb_line_textfield.setFormCode(formCode);
				rn_fb_line_textfield.setKey1("");
//				rn_fb_line_textfield.setMandatory("N");
//				rn_fb_line_textfield.setHidden("N");
//				rn_fb_line_textfield.setReadonly("N");
//				rn_fb_line_textfield.setDependent("N");
				rn_fb_line_textfield.setMandatory(false);
				rn_fb_line_textfield.setHidden(false);
				rn_fb_line_textfield.setReadonly(false);
				rn_fb_line_textfield.setDependent(false);
//				rn_fb_line_textfield.setDependent_on("");
//				rn_fb_line_textfield.setDependent_sp("");
//				rn_fb_line_textfield.setDependent_sp_param("");
				rn_fb_line_textfield.setValidation_1(false);
//				rn_fb_line_textfield.setVal_type("");
//				rn_fb_line_textfield.setVal_sp("");
//				rn_fb_line_textfield.setVal_sp_param("");
				rn_fb_line_textfield.setSequence(false);
//				rn_fb_line_textfield.setSeq_name("");
//				rn_fb_line_textfield.setSeq_sp("");
//				rn_fb_line_textfield.setSeq_sp_param("");
				rn_fb_line_textfield.setDefault_1(false);
//				rn_fb_line_textfield.setDefault_type("");
//				rn_fb_line_textfield.setDefault_value("");
//				rn_fb_line_textfield.setDefault_sp("");
//				rn_fb_line_textfield.setDefault_sp_param("");
				rn_fb_line_textfield.setCalculated_field(false);
//				rn_fb_line_textfield.setCal_sp("");
//				rn_fb_line_textfield.setCal_sp_param("");
//				rn_fb_line_textfield.setAdd_to_grid("Y");
//				rn_fb_line_textfield.setSp_for_autocomplete("N");
//				rn_fb_line_textfield.setSp_for_dropdown("N");
				rn_fb_line_section.setAdd_to_grid(true);
				rn_fb_line_section.setSp_for_autocomplete(false);
				rn_fb_line_section.setSp_for_dropdown(false);
//				rn_fb_line_textfield.setSp_name_for_dropdown("");
//				rn_fb_line_textfield.setSp_name_for_autocomplete("");
//				rn_fb_line_textfield.setLine_table_name("");
				rn_fb_line_textfield.setLine_table_no(0);
				// WHO
				rn_fb_line_textfield.setCreatedBy(userId);
				rn_fb_line_textfield.setUpdatedBy(userId);
//				rn_fb_line_textfield.setAccountId(accountId);
				rn_fb_line_textfield.setRn_fb_header(savedHeader);
				this.saveLine(rn_fb_line_textfield);
			}

			/** ONLY-LINE Button */
			Rn_Fb_Line rn_fb_line_button = new Rn_Fb_Line();
//			rn_fb_line_button.setHeader_id(header_id);
//			rn_fb_line_button.setProject_id(project_id);
//			rn_fb_line_button.setModule_id(module_id);
//			rn_fb_line_button.setAccount_id(0);
			rn_fb_line_button.setMapping("button1");
			rn_fb_line_button.setSeq(0);
			rn_fb_line_button.setForm_type(formType);
			rn_fb_line_button.setFieldName("submit");
			rn_fb_line_button.setType_field(WireFrameConstant.BUTTON);
			rn_fb_line_button.setSection_num(0);
			rn_fb_line_button.setButton_num(1);
			rn_fb_line_button.setType1(formType);
			rn_fb_line_button.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_button.setDataType("N");
			rn_fb_line_button.setFormCode(formCode);
			rn_fb_line_button.setKey1("");
//			rn_fb_line_button.setMandatory("N");
//			rn_fb_line_button.setHidden("N");
//			rn_fb_line_button.setReadonly("N");
//			rn_fb_line_button.setDependent("N");
			rn_fb_line_button.setMandatory(false);
			rn_fb_line_button.setHidden(false);
			rn_fb_line_button.setReadonly(false);
			rn_fb_line_button.setDependent(false);
//			rn_fb_line_button.setDependent_on("");
//			rn_fb_line_button.setDependent_sp("");
//			rn_fb_line_button.setDependent_sp_param("");
			rn_fb_line_button.setValidation_1(false);
//			rn_fb_line_button.setVal_type("");
//			rn_fb_line_button.setVal_sp("");
//			rn_fb_line_button.setVal_sp_param("");
			rn_fb_line_button.setSequence(false);
//			rn_fb_line_button.setSeq_name("");
//			rn_fb_line_button.setSeq_sp("");
//			rn_fb_line_button.setSeq_sp_param("");
			rn_fb_line_button.setDefault_1(false);
//			rn_fb_line_button.setDefault_type("");
//			rn_fb_line_button.setDefault_value("");
//			rn_fb_line_button.setDefault_sp("");
//			rn_fb_line_button.setDefault_sp_param("");
			rn_fb_line_button.setCalculated_field(false);
//			rn_fb_line_button.setCal_sp("");
//			rn_fb_line_button.setCal_sp_param("");
//			rn_fb_line_button.setAdd_to_grid("Y");
//			rn_fb_line_button.setSp_for_autocomplete("N");
//			rn_fb_line_button.setSp_for_dropdown("N");
			rn_fb_line_button.setAdd_to_grid(true);
			rn_fb_line_button.setSp_for_autocomplete(false);
			rn_fb_line_button.setSp_for_dropdown(false);
//			rn_fb_line_button.setSp_name_for_dropdown("");
//			rn_fb_line_button.setSp_name_for_autocomplete("");
//			rn_fb_line_button.setLine_table_name("");
			rn_fb_line_button.setLine_table_no(0);
			// WHO
			rn_fb_line_button.setCreatedBy(userId);
			rn_fb_line_button.setUpdatedBy(userId);
//			rn_fb_line_button.setAccountId(accountId);
			rn_fb_line_button.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_button);

		} // ONLY-LINE END

		/** HEADER-LINE Default values */
		else if (WireFrameConstant.HEADER_LINE.equalsIgnoreCase(formType)) {
			/** Header Section in H-L */
			Rn_Fb_Line rn_fb_line_section = new Rn_Fb_Line();
//			rn_fb_line_section.setHeader_id(header_id);
//			rn_fb_line_section.setProject_id(project_id);
//			rn_fb_line_section.setModule_id(module_id);
//			rn_fb_line_section.setAccount_id(0);
			rn_fb_line_section.setMapping("section1");
			rn_fb_line_section.setSeq(0);
			rn_fb_line_section.setFieldName(WireFrameConstant.SECTION + "1");
			rn_fb_line_section.setType_field(WireFrameConstant.SECTION);
			rn_fb_line_section.setSection_num(1);
			rn_fb_line_section.setButton_num(0);
			rn_fb_line_section.setForm_type(formType);
			rn_fb_line_section.setType1(formType);
			rn_fb_line_section.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_section.setDataType("N");
			rn_fb_line_section.setFormCode(formCode);
			rn_fb_line_section.setKey1("");
//			rn_fb_line_section.setMandatory("N");
//			rn_fb_line_section.setHidden("N");
//			rn_fb_line_section.setReadonly("N");
//			rn_fb_line_section.setDependent("N");
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);
//			rn_fb_line_section.setDependent_on("");
//			rn_fb_line_section.setDependent_sp("");
//			rn_fb_line_section.setDependent_sp_param("");
			rn_fb_line_section.setValidation_1(false);
//			rn_fb_line_section.setVal_type("");
//			rn_fb_line_section.setVal_sp("");
//			rn_fb_line_section.setVal_sp_param("");
			rn_fb_line_section.setSequence(false);
//			rn_fb_line_section.setSeq_name("");
//			rn_fb_line_section.setSeq_sp("");
//			rn_fb_line_section.setSeq_sp_param("");
			rn_fb_line_section.setDefault_1(false);
//			rn_fb_line_section.setDefault_type("");
//			rn_fb_line_section.setDefault_value("");
//			rn_fb_line_section.setDefault_sp("");
//			rn_fb_line_section.setDefault_sp_param("");
			rn_fb_line_section.setCalculated_field(false);
//			rn_fb_line_section.setCal_sp("");
//			rn_fb_line_section.setCal_sp_param("");
//			rn_fb_line_section.setAdd_to_grid("Y");
//			rn_fb_line_section.setSp_for_autocomplete("N");
//			rn_fb_line_section.setSp_for_dropdown("N");
			rn_fb_line_section.setAdd_to_grid(true);
			rn_fb_line_section.setSp_for_autocomplete(false);
			rn_fb_line_section.setSp_for_dropdown(false);
//			rn_fb_line_section.setSp_name_for_dropdown("");
//			rn_fb_line_section.setSp_name_for_autocomplete("");
//			rn_fb_line_section.setLine_table_name("");
			rn_fb_line_section.setLine_table_no(0);
			// WHO
			rn_fb_line_section.setCreatedBy(userId);
			rn_fb_line_section.setUpdatedBy(userId);
//			rn_fb_line_section.setAccountId(accountId);
			rn_fb_line_section.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_section);
			/** Header Line Primary Id */
			Rn_Fb_Line rn_fb_line_pk = new Rn_Fb_Line();
//			rn_fb_line_pk.setHeader_id(header_id);
//			rn_fb_line_pk.setProject_id(project_id);
//			rn_fb_line_pk.setModule_id(module_id);
//			rn_fb_line_pk.setAccount_id(0);
			rn_fb_line_pk.setMapping("id");
			rn_fb_line_pk.setSeq(0);
			rn_fb_line_pk.setFieldName("select");
			rn_fb_line_pk.setType_field("id");
			rn_fb_line_pk.setForm_type(formType);
			rn_fb_line_pk.setType1(formType);
			rn_fb_line_pk.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_pk.setSection_num(1);
			rn_fb_line_pk.setButton_num(0);
			rn_fb_line_pk.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_pk.setFormCode(formCode);
			rn_fb_line_pk.setKey1("PRI");
//			rn_fb_line_pk.setMandatory("N");
//			rn_fb_line_pk.setHidden("N");
//			rn_fb_line_pk.setReadonly("N");
//			rn_fb_line_pk.setDependent("N");
			rn_fb_line_pk.setMandatory(false);
			rn_fb_line_pk.setHidden(false);
			rn_fb_line_pk.setReadonly(false);
			rn_fb_line_pk.setDependent(false);
//			rn_fb_line_pk.setDependent_on("");
//			rn_fb_line_pk.setDependent_sp("");
//			rn_fb_line_pk.setDependent_sp_param("");
			rn_fb_line_pk.setValidation_1(false);
//			rn_fb_line_pk.setVal_type("");
//			rn_fb_line_pk.setVal_sp("");
//			rn_fb_line_pk.setVal_sp_param("");
			rn_fb_line_pk.setSequence(false);
//			rn_fb_line_pk.setSeq_name("");
//			rn_fb_line_pk.setSeq_sp("");
//			rn_fb_line_pk.setSeq_sp_param("");
			rn_fb_line_pk.setDefault_1(false);
//			rn_fb_line_pk.setDefault_type("");
//			rn_fb_line_pk.setDefault_value("");
//			rn_fb_line_pk.setDefault_sp("");
//			rn_fb_line_pk.setDefault_sp_param("");
			rn_fb_line_pk.setCalculated_field(false);
//			rn_fb_line_pk.setCal_sp("");
//			rn_fb_line_pk.setCal_sp_param("");
//			rn_fb_line_pk.setAdd_to_grid("Y");
//			rn_fb_line_pk.setSp_for_autocomplete("N");
//			rn_fb_line_pk.setSp_for_dropdown("N");
			rn_fb_line_pk.setAdd_to_grid(true);
			rn_fb_line_pk.setSp_for_autocomplete(false);
			rn_fb_line_pk.setSp_for_dropdown(false);
//			rn_fb_line_pk.setSp_name_for_dropdown("");
//			rn_fb_line_pk.setSp_name_for_autocomplete("");
//			rn_fb_line_pk.setLine_table_name("");
			rn_fb_line_pk.setLine_table_no(0);

			// WHO
			rn_fb_line_pk.setCreatedBy(userId);
			rn_fb_line_pk.setUpdatedBy(userId);
//			rn_fb_line_pk.setAccountId(accountId);

			rn_fb_line_pk.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_pk);

			/** Header TextField - 4 default field */
			for (int i = 1; i <= 4; i++) {
				Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();
//				rn_fb_line_textfield.setHeader_id(header_id);
//				rn_fb_line_textfield.setProject_id(project_id);
//				rn_fb_line_textfield.setModule_id(module_id);
//				rn_fb_line_textfield.setAccount_id(0);
				rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + i);
				rn_fb_line_textfield.setSeq(i);
				rn_fb_line_textfield.setFieldName(WireFrameConstant.LABEL + i);
				rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
				rn_fb_line_textfield.setSection_num(1);
				rn_fb_line_textfield.setButton_num(0);
				rn_fb_line_textfield.setForm_type(formType);
				rn_fb_line_textfield.setType1(formType);
				rn_fb_line_textfield.setType2(WireFrameConstant.HEADER_ONLY);
				rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
				rn_fb_line_textfield.setFormCode(formCode);
				rn_fb_line_textfield.setKey1("");
//				rn_fb_line_textfield.setMandatory("N");
//				rn_fb_line_textfield.setHidden("N");
//				rn_fb_line_textfield.setReadonly("N");
//				rn_fb_line_textfield.setDependent("N");
				rn_fb_line_textfield.setMandatory(false);
				rn_fb_line_textfield.setHidden(false);
				rn_fb_line_textfield.setReadonly(false);
				rn_fb_line_textfield.setDependent(false);
//				rn_fb_line_textfield.setDependent_on("");
//				rn_fb_line_textfield.setDependent_sp("");
//				rn_fb_line_textfield.setDependent_sp_param("");
				rn_fb_line_textfield.setValidation_1(false);
//				rn_fb_line_textfield.setVal_type("");
//				rn_fb_line_textfield.setVal_sp("");
//				rn_fb_line_textfield.setVal_sp_param("");
				rn_fb_line_textfield.setSequence(false);
//				rn_fb_line_textfield.setSeq_name("");
//				rn_fb_line_textfield.setSeq_sp("");
//				rn_fb_line_textfield.setSeq_sp_param("");
				rn_fb_line_textfield.setDefault_1(false);
//				rn_fb_line_textfield.setDefault_type("");
//				rn_fb_line_textfield.setDefault_value("");
//				rn_fb_line_textfield.setDefault_sp("");
//				rn_fb_line_textfield.setDefault_sp_param("");
				rn_fb_line_textfield.setCalculated_field(false);
//				rn_fb_line_textfield.setCal_sp("");
//				rn_fb_line_textfield.setCal_sp_param("");
//				rn_fb_line_textfield.setAdd_to_grid("Y");
//				rn_fb_line_textfield.setSp_for_autocomplete("N");
//				rn_fb_line_textfield.setSp_for_dropdown("N");
				rn_fb_line_textfield.setAdd_to_grid(true);
				rn_fb_line_textfield.setSp_for_autocomplete(false);
				rn_fb_line_textfield.setSp_for_dropdown(false);
//				rn_fb_line_textfield.setSp_name_for_dropdown("");
//				rn_fb_line_textfield.setSp_name_for_autocomplete("");
//				rn_fb_line_textfield.setLine_table_name("");
				rn_fb_line_textfield.setLine_table_no(0);
				// WHO
				rn_fb_line_textfield.setCreatedBy(userId);
				rn_fb_line_textfield.setUpdatedBy(userId);
//				rn_fb_line_textfield.setAccountId(accountId);

				rn_fb_line_textfield.setRn_fb_header(savedHeader);
				this.saveLine(rn_fb_line_textfield);

			}

			/** --- Line Part in HEADER-LINE FORM --- */
			/** Line Section in H-L Form */
			Rn_Fb_Line rn_fb_line_section_l = new Rn_Fb_Line();
//			rn_fb_line_section_l.setHeader_id(header_id);
//			rn_fb_line_section_l.setProject_id(project_id);
//			rn_fb_line_section_l.setModule_id(module_id);
			rn_fb_line_section_l.setMapping(WireFrameConstant.LINE_SECTION);
			rn_fb_line_section_l.setSeq(0);
			rn_fb_line_section_l.setFieldName(WireFrameConstant.LINE_SECTION);
			rn_fb_line_section_l.setType_field(WireFrameConstant.LINE_SECTION);
			rn_fb_line_section_l.setSection_num(0);
			rn_fb_line_section_l.setButton_num(0);
			rn_fb_line_section_l.setForm_type(formType);
			rn_fb_line_section_l.setType1(formType);
			rn_fb_line_section_l.setType2("line");
			rn_fb_line_section_l.setDataType("N");
			rn_fb_line_section_l.setFormCode(formCode);
			rn_fb_line_section_l.setKey1("");
//			rn_fb_line_section_l.setMandatory("N");
//			rn_fb_line_section_l.setHidden("N");
//			rn_fb_line_section_l.setReadonly("N");
//			rn_fb_line_section_l.setDependent("N");
			rn_fb_line_section_l.setMandatory(false);
			rn_fb_line_section_l.setHidden(false);
			rn_fb_line_section_l.setReadonly(false);
			rn_fb_line_section_l.setDependent(false);
//			rn_fb_line_section_l.setDependent_on("");
//			rn_fb_line_section_l.setDependent_sp("");
//			rn_fb_line_section_l.setDependent_sp_param("");
			rn_fb_line_section_l.setValidation_1(false);
//			rn_fb_line_section_l.setVal_type("");
//			rn_fb_line_section_l.setVal_sp("");
//			rn_fb_line_section_l.setVal_sp_param("");
			rn_fb_line_section_l.setSequence(false);
//			rn_fb_line_section_l.setSeq_name("");
//			rn_fb_line_section_l.setSeq_sp("");
//			rn_fb_line_section_l.setSeq_sp_param("");
			rn_fb_line_section_l.setDefault_1(false);
//			rn_fb_line_section_l.setDefault_type("");
//			rn_fb_line_section_l.setDefault_value("");
//			rn_fb_line_section_l.setDefault_sp("");
//			rn_fb_line_section_l.setDefault_sp_param("");
			rn_fb_line_section_l.setCalculated_field(false);
//			rn_fb_line_section_l.setCal_sp("");
//			rn_fb_line_section_l.setCal_sp_param("");
//			rn_fb_line_section_l.setAdd_to_grid("Y");
//			rn_fb_line_section_l.setSp_for_autocomplete("N");
//			rn_fb_line_section_l.setSp_for_dropdown("N");
			rn_fb_line_section_l.setAdd_to_grid(true);
			rn_fb_line_section_l.setSp_for_autocomplete(false);
			rn_fb_line_section_l.setSp_for_dropdown(false);
//			rn_fb_line_section_l.setSp_name_for_dropdown("");
//			rn_fb_line_section_l.setSp_name_for_autocomplete("");
//			rn_fb_line_section_l.setLine_table_name("");
			rn_fb_line_section_l.setLine_table_no(1);
			// WHO
			rn_fb_line_section_l.setCreatedBy(userId);
			rn_fb_line_section_l.setUpdatedBy(userId);
//			rn_fb_line_section_l.setAccountId(accountId);
			rn_fb_line_section_l.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_section_l);

			/** Line Part Primary Id in H-L Form */
			Rn_Fb_Line rn_fb_line_pk_l = new Rn_Fb_Line();
//			rn_fb_line_pk_l.setHeader_id(header_id);
//			rn_fb_line_pk_l.setProject_id(project_id);
//			rn_fb_line_pk_l.setModule_id(module_id);
//			rn_fb_line_pk_l.setAccount_id(0);
			rn_fb_line_pk_l.setMapping("l_id");
			rn_fb_line_pk_l.setSeq(0);
			rn_fb_line_pk_l.setFieldName("select");
			rn_fb_line_pk_l.setType_field("l_id");
			rn_fb_line_pk_l.setSection_num(1);
			rn_fb_line_pk_l.setButton_num(0);
			rn_fb_line_pk_l.setForm_type(formType);
			rn_fb_line_pk_l.setType1(formType);
			rn_fb_line_pk_l.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_pk_l.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_pk_l.setFormCode(formCode);
			rn_fb_line_pk_l.setKey1(WireFrameConstant.DT_PK);
//			rn_fb_line_pk_l.setMandatory("N");
//			rn_fb_line_pk_l.setHidden("N");
//			rn_fb_line_pk_l.setReadonly("N");
//			rn_fb_line_pk_l.setDependent("N");
			rn_fb_line_pk_l.setMandatory(false);
			rn_fb_line_pk_l.setHidden(false);
			rn_fb_line_pk_l.setReadonly(false);
			rn_fb_line_pk_l.setDependent(false);
//			rn_fb_line_pk_l.setDependent_on("");
//			rn_fb_line_pk_l.setDependent_sp("");
//			rn_fb_line_pk_l.setDependent_sp_param("");
			rn_fb_line_pk_l.setValidation_1(false);
//			rn_fb_line_pk_l.setVal_type("");
//			rn_fb_line_pk_l.setVal_sp("");
//			rn_fb_line_pk_l.setVal_sp_param("");
			rn_fb_line_pk_l.setSequence(false);
//			rn_fb_line_pk_l.setSeq_name("");
//			rn_fb_line_pk_l.setSeq_sp("");
//			rn_fb_line_pk_l.setSeq_sp_param("");
			rn_fb_line_pk_l.setDefault_1(false);
//			rn_fb_line_pk_l.setDefault_type("");
//			rn_fb_line_pk_l.setDefault_value("");
//			rn_fb_line_pk_l.setDefault_sp("");
//			rn_fb_line_pk_l.setDefault_sp_param("");
			rn_fb_line_pk_l.setCalculated_field(false);
//			rn_fb_line_pk_l.setCal_sp("");
//			rn_fb_line_pk_l.setCal_sp_param("");
//			rn_fb_line_pk_l.setAdd_to_grid("Y");
//			rn_fb_line_pk_l.setSp_for_autocomplete("N");
//			rn_fb_line_pk_l.setSp_for_dropdown("N");
			rn_fb_line_pk_l.setAdd_to_grid(true);
			rn_fb_line_pk_l.setSp_for_autocomplete(false);
			rn_fb_line_pk_l.setSp_for_dropdown(false);
//			rn_fb_line_pk_l.setSp_name_for_dropdown("");
//			rn_fb_line_pk_l.setSp_name_for_autocomplete("");
//			rn_fb_line_pk_l.setLine_table_name("");
			rn_fb_line_pk_l.setLine_table_no(1);
			// WHO
			rn_fb_line_pk_l.setCreatedBy(userId);
			rn_fb_line_pk_l.setUpdatedBy(userId);
//			rn_fb_line_pk_l.setAccountId(accountId);
			rn_fb_line_pk_l.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_pk_l);

			/**
			 * Line Part Header Id(foreign key) to primary id of header values of HeaderLine
			 * WireFrame
			 *
			 */
			Rn_Fb_Line rn_fb_line_fk = new Rn_Fb_Line();
			rn_fb_line_fk.setMapping("id");
			rn_fb_line_fk.setSeq(0);

			rn_fb_line_fk.setFieldName("hid");
			rn_fb_line_fk.setType_field("hid");
			rn_fb_line_fk.setSection_num(1);
			rn_fb_line_fk.setButton_num(0);
			rn_fb_line_fk.setForm_type(formType);
			rn_fb_line_fk.setType1(formType);
			rn_fb_line_fk.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_fk.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_fk.setFormCode(formCode);
			rn_fb_line_fk.setKey1("hid");
//			rn_fb_line_fk.setMandatory("N");
//			rn_fb_line_fk.setHidden("N");
//			rn_fb_line_fk.setReadonly("N");
//			rn_fb_line_fk.setDependent("N");
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);
//			rn_fb_line_fk.setDependent_on("");
//			rn_fb_line_fk.setDependent_sp("");
//			rn_fb_line_fk.setDependent_sp_param("");
			rn_fb_line_fk.setValidation_1(false);
//			rn_fb_line_fk.setVal_type("");
//			rn_fb_line_fk.setVal_sp("");
//			rn_fb_line_fk.setVal_sp_param("");
			rn_fb_line_fk.setSequence(false);
//			rn_fb_line_fk.setSeq_name("");
//			rn_fb_line_fk.setSeq_sp("");
//			rn_fb_line_fk.setSeq_sp_param("");
			rn_fb_line_fk.setDefault_1(false);
//			rn_fb_line_fk.setDefault_type("");
//			rn_fb_line_fk.setDefault_value("");
//			rn_fb_line_fk.setDefault_sp("");
//			rn_fb_line_fk.setDefault_sp_param("");
			rn_fb_line_fk.setCalculated_field(false);
//			rn_fb_line_fk.setCal_sp("");
//			rn_fb_line_fk.setCal_sp_param("");
//			rn_fb_line_fk.setAdd_to_grid("Y");
//			rn_fb_line_fk.setSp_for_autocomplete("N");
//			rn_fb_line_fk.setSp_for_dropdown("N");
			rn_fb_line_fk.setAdd_to_grid(true);
			rn_fb_line_fk.setSp_for_autocomplete(false);
			rn_fb_line_fk.setSp_for_dropdown(false);
//			rn_fb_line_fk.setSp_name_for_dropdown("");
//			rn_fb_line_fk.setSp_name_for_autocomplete("");
//			rn_fb_line_fk.setLine_table_name("");
			rn_fb_line_fk.setLine_table_no(1);
			// WHO
			rn_fb_line_fk.setCreatedBy(userId);
			rn_fb_line_fk.setUpdatedBy(userId);
//			rn_fb_line_fk.setAccountId(accountId);

			rn_fb_line_fk.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_fk);

			/**
			 * Line Part TextField in H-L Form
			 * 
			 */
			for (int i = 5; i <= 8; i++) {
				Rn_Fb_Line rn_fb_line_textfield_l = new Rn_Fb_Line();
				rn_fb_line_textfield_l.setMapping(WireFrameConstant.DT_TEXTFIELD + i);
				rn_fb_line_textfield_l.setSeq(i);
				rn_fb_line_textfield_l.setFieldName("column" + i);
				rn_fb_line_textfield_l.setType_field(WireFrameConstant.DT_TEXTFIELD);
				rn_fb_line_textfield_l.setSection_num(0);
				rn_fb_line_textfield_l.setButton_num(0);
				rn_fb_line_textfield_l.setForm_type(formType);
				rn_fb_line_textfield_l.setType1(formType);
				rn_fb_line_textfield_l.setType2(WireFrameConstant.LINE_TYPE);
				rn_fb_line_textfield_l.setDataType(WireFrameConstant.DT_VARCHAR);
				rn_fb_line_textfield_l.setFormCode(formCode);
				rn_fb_line_textfield_l.setKey1("");
//				rn_fb_line_textfield_l.setMandatory("N");
//				rn_fb_line_textfield_l.setHidden("N");
//				rn_fb_line_textfield_l.setReadonly("N");
//				rn_fb_line_textfield_l.setDependent("N");
				rn_fb_line_textfield_l.setMandatory(false);
				rn_fb_line_textfield_l.setHidden(false);
				rn_fb_line_textfield_l.setReadonly(false);
				rn_fb_line_textfield_l.setDependent(false);
//				rn_fb_line_textfield_l.setDependent_on("");
//				rn_fb_line_textfield_l.setDependent_sp("");
//				rn_fb_line_textfield_l.setDependent_sp_param("");
				rn_fb_line_textfield_l.setValidation_1(false);
//				rn_fb_line_textfield_l.setVal_type("");
//				rn_fb_line_textfield_l.setVal_sp("");
//				rn_fb_line_textfield_l.setVal_sp_param("");
				rn_fb_line_textfield_l.setSequence(false);
//				rn_fb_line_textfield_l.setSeq_name("");
//				rn_fb_line_textfield_l.setSeq_sp("");
//				rn_fb_line_textfield_l.setSeq_sp_param("");
				rn_fb_line_textfield_l.setDefault_1(false);
//				rn_fb_line_textfield_l.setDefault_type("");
//				rn_fb_line_textfield_l.setDefault_value("");
//				rn_fb_line_textfield_l.setDefault_sp("");
//				rn_fb_line_textfield_l.setDefault_sp_param("");
				rn_fb_line_textfield_l.setCalculated_field(false);
				rn_fb_line_textfield_l.setCal_sp("");
				rn_fb_line_textfield_l.setCal_sp_param("");
//				rn_fb_line_textfield_l.setAdd_to_grid("Y");
//				rn_fb_line_textfield_l.setSp_for_autocomplete("N");
//				rn_fb_line_textfield_l.setSp_for_dropdown("N");
				rn_fb_line_textfield_l.setAdd_to_grid(true);
				rn_fb_line_textfield_l.setSp_for_autocomplete(false);
				rn_fb_line_textfield_l.setSp_for_dropdown(false);
//				rn_fb_line_textfield_l.setSp_name_for_dropdown("");
//				rn_fb_line_textfield_l.setSp_name_for_autocomplete("");
//				rn_fb_line_textfield_l.setLine_table_name("");
				rn_fb_line_textfield_l.setLine_table_no(1);
				// WHO
				rn_fb_line_textfield_l.setCreatedBy(userId);
				rn_fb_line_textfield_l.setUpdatedBy(userId);
//				rn_fb_line_textfield_l.setAccountId(accountId);
				rn_fb_line_textfield_l.setRn_fb_header(savedHeader);
				this.saveLine(rn_fb_line_textfield_l);

			}

			/** Header Button in H-L Form */
			Rn_Fb_Line rn_fb_line_button = new Rn_Fb_Line();
			rn_fb_line_button.setMapping("button1");
			rn_fb_line_button.setSeq(0);
			rn_fb_line_button.setFieldName("submit");
			rn_fb_line_button.setType_field(WireFrameConstant.BUTTON);
			rn_fb_line_button.setSection_num(0);
			rn_fb_line_button.setButton_num(1);
			rn_fb_line_button.setForm_type(formType);
			rn_fb_line_button.setType1(formType);
			rn_fb_line_button.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_button.setDataType("N");
			rn_fb_line_button.setFormCode(formCode);
			rn_fb_line_button.setKey1("");
//			rn_fb_line_button.setMandatory("N");
//			rn_fb_line_button.setHidden("N");
//			rn_fb_line_button.setReadonly("N");
//			rn_fb_line_button.setDependent("N");
			rn_fb_line_button.setMandatory(false);
			rn_fb_line_button.setHidden(false);
			rn_fb_line_button.setReadonly(false);
			rn_fb_line_button.setDependent(false);
//			rn_fb_line_button.setDependent_on("");
//			rn_fb_line_button.setDependent_sp("");
//			rn_fb_line_button.setDependent_sp_param("");
			rn_fb_line_button.setValidation_1(false);
//			rn_fb_line_button.setVal_type("");
//			rn_fb_line_button.setVal_sp("");
//			rn_fb_line_button.setVal_sp_param("");
			rn_fb_line_button.setSequence(false);
//			rn_fb_line_button.setSeq_name("");
//			rn_fb_line_button.setSeq_sp("");
//			rn_fb_line_button.setSeq_sp_param("");
			rn_fb_line_button.setDefault_1(false);
//			rn_fb_line_button.setDefault_type("");
//			rn_fb_line_button.setDefault_value("");
//			rn_fb_line_button.setDefault_sp("");
//			rn_fb_line_button.setDefault_sp_param("");
			rn_fb_line_button.setCalculated_field(false);
//			rn_fb_line_button.setCal_sp("");
//			rn_fb_line_button.setCal_sp_param("");
//			rn_fb_line_button.setAdd_to_grid("Y");
//			rn_fb_line_button.setSp_for_autocomplete("N");
//			rn_fb_line_button.setSp_for_dropdown("N");
			rn_fb_line_button.setAdd_to_grid(true);
			rn_fb_line_button.setSp_for_autocomplete(false);
			rn_fb_line_button.setSp_for_dropdown(false);
//			rn_fb_line_button.setSp_name_for_dropdown("");
//			rn_fb_line_button.setSp_name_for_autocomplete("");
//			rn_fb_line_button.setLine_table_name("");
			rn_fb_line_button.setLine_table_no(1);
			// WHO
			rn_fb_line_button.setCreatedBy(userId);
			rn_fb_line_button.setUpdatedBy(userId);
//			rn_fb_line_button.setAccountId(accountId);
			rn_fb_line_button.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_button);
		} // H-L END

		/**
		 * MultiLine WireFrame Default Values
		 */
		else if (WireFrameConstant.MULTILINE.equalsIgnoreCase(formType)) {
			/** Header Part in MultiLine */
			Rn_Fb_Line rn_fb_line_hn = new Rn_Fb_Line();
			rn_fb_line_hn.setMapping(uiName);
			rn_fb_line_hn.setSeq(0);
			rn_fb_line_hn.setFieldName(uiName + "_title");
			rn_fb_line_hn.setType_field("header_name");
			rn_fb_line_hn.setSection_num(0);
			rn_fb_line_hn.setButton_num(0);
			rn_fb_line_hn.setForm_type(formType);
			rn_fb_line_hn.setType1(formType);
			rn_fb_line_hn.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_hn.setDataType("N");
			rn_fb_line_hn.setFormCode(formCode);
			rn_fb_line_hn.setKey1("");
//			rn_fb_line_hn.setMandatory("N");
//			rn_fb_line_hn.setHidden("N");
//			rn_fb_line_hn.setReadonly("N");
//			rn_fb_line_hn.setDependent("N");
			rn_fb_line_hn.setMandatory(false);
			rn_fb_line_hn.setHidden(false);
			rn_fb_line_hn.setReadonly(false);
			rn_fb_line_hn.setDependent(false);
//			rn_fb_line_hn.setDependent_on("");
//			rn_fb_line_hn.setDependent_sp("");
//			rn_fb_line_hn.setDependent_sp_param("");
			rn_fb_line_hn.setValidation_1(false);
//			rn_fb_line_hn.setVal_type("");
//			rn_fb_line_hn.setVal_sp("");
//			rn_fb_line_hn.setVal_sp_param("");
			rn_fb_line_hn.setSequence(false);
//			rn_fb_line_hn.setSeq_name("");
//			rn_fb_line_hn.setSeq_sp("");
//			rn_fb_line_hn.setSeq_sp_param("");
			rn_fb_line_hn.setDefault_1(false);
//			rn_fb_line_hn.setDefault_type("");
//			rn_fb_line_hn.setDefault_value("");
//			rn_fb_line_hn.setDefault_sp("");
//			rn_fb_line_hn.setDefault_sp_param("");
			rn_fb_line_hn.setCalculated_field(false);
//			rn_fb_line_hn.setCal_sp("");
//			rn_fb_line_hn.setCal_sp_param("");
//			rn_fb_line_hn.setAdd_to_grid("Y");
//			rn_fb_line_hn.setSp_for_autocomplete("N");
//			rn_fb_line_hn.setSp_for_dropdown("N");
			rn_fb_line_hn.setAdd_to_grid(true);
			rn_fb_line_hn.setSp_for_autocomplete(false);
			rn_fb_line_hn.setSp_for_dropdown(false);
//			rn_fb_line_hn.setSp_name_for_dropdown("");
//			rn_fb_line_hn.setSp_name_for_autocomplete("");
//			rn_fb_line_hn.setLine_table_name("");
			rn_fb_line_hn.setLine_table_no(0);
			// WHO
			rn_fb_line_hn.setCreatedBy(userId);
			rn_fb_line_hn.setUpdatedBy(userId);
//			rn_fb_line_hn.setAccountId(accountId);
			rn_fb_line_hn.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_hn);

			/* MultiLine Section */
			Rn_Fb_Line rn_fb_line_section = new Rn_Fb_Line();
			rn_fb_line_section.setMapping(WireFrameConstant.SECTION);
			rn_fb_line_section.setSeq(0);
			rn_fb_line_section.setFieldName(WireFrameConstant.SECTION);
			rn_fb_line_section.setType_field(WireFrameConstant.SECTION);
			rn_fb_line_section.setSection_num(0);
			rn_fb_line_section.setButton_num(0);
			rn_fb_line_section.setForm_type(formType);
			rn_fb_line_section.setType1(formType);
			rn_fb_line_section.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_section.setDataType("N");
			rn_fb_line_section.setFormCode(formCode);
			rn_fb_line_section.setKey1("");
//			rn_fb_line_section.setMandatory("N");
//			rn_fb_line_section.setHidden("N");
//			rn_fb_line_section.setReadonly("N");
//			rn_fb_line_section.setDependent("N");
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);
//			rn_fb_line_section.setDependent_on("");
//			rn_fb_line_section.setDependent_sp("");
//			rn_fb_line_section.setDependent_sp_param("");
			rn_fb_line_section.setValidation_1(false);
//			rn_fb_line_section.setVal_type("");
//			rn_fb_line_section.setVal_sp("");
//			rn_fb_line_section.setVal_sp_param("");
			rn_fb_line_section.setSequence(false);
//			rn_fb_line_section.setSeq_name("");
//			rn_fb_line_section.setSeq_sp("");
//			rn_fb_line_section.setSeq_sp_param("");
			rn_fb_line_section.setDefault_1(false);
//			rn_fb_line_section.setDefault_type("");
//			rn_fb_line_section.setDefault_value("");
//			rn_fb_line_section.setDefault_sp("");
//			rn_fb_line_section.setDefault_sp_param("");
			rn_fb_line_section.setCalculated_field(false);
			rn_fb_line_section.setCal_sp("");
			rn_fb_line_section.setCal_sp_param("");
//			rn_fb_line_section.setAdd_to_grid("Y");
//			rn_fb_line_section.setSp_for_autocomplete("N");
//			rn_fb_line_section.setSp_for_dropdown("N");
			rn_fb_line_section.setAdd_to_grid(true);
			rn_fb_line_section.setSp_for_autocomplete(false);
			rn_fb_line_section.setSp_for_dropdown(false);
//			rn_fb_line_section.setSp_name_for_dropdown("");
//			rn_fb_line_section.setSp_name_for_autocomplete("");
//			rn_fb_line_section.setLine_table_name("");
			rn_fb_line_section.setLine_table_no(0);
			// WHO
			rn_fb_line_section.setCreatedBy(userId);
			rn_fb_line_section.setUpdatedBy(userId);
//			rn_fb_line_section.setAccountId(accountId);
			rn_fb_line_section.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_section);

			/**
			 * MultiLine Header Part Primary Id
			 */
			Rn_Fb_Line rn_fb_line_pk = new Rn_Fb_Line();
			rn_fb_line_pk.setMapping("id");
			rn_fb_line_pk.setSeq(0);
			rn_fb_line_pk.setFieldName("select");
			rn_fb_line_pk.setType_field("id");
			rn_fb_line_pk.setSection_num(0);
			rn_fb_line_pk.setButton_num(0);
			rn_fb_line_pk.setForm_type(formType);
			rn_fb_line_pk.setType1(formType);
			rn_fb_line_pk.setType2(WireFrameConstant.HEADER_TYPE);
			rn_fb_line_pk.setDataType(WireFrameConstant.DT_INTEGER);
			rn_fb_line_pk.setFormCode(formCode);
			rn_fb_line_pk.setKey1(WireFrameConstant.DT_PK);
//			rn_fb_line_pk.setMandatory("N");
//			rn_fb_line_pk.setHidden("N");
//			rn_fb_line_pk.setReadonly("N");
//			rn_fb_line_pk.setDependent("N");
			rn_fb_line_pk.setMandatory(false);
			rn_fb_line_pk.setHidden(false);
			rn_fb_line_pk.setReadonly(false);
			rn_fb_line_pk.setDependent(false);
//			rn_fb_line_pk.setDependent_on("");
//			rn_fb_line_pk.setDependent_sp("");
//			rn_fb_line_pk.setDependent_sp_param("");
			rn_fb_line_pk.setValidation_1(false);
//			rn_fb_line_pk.setVal_type("");
//			rn_fb_line_pk.setVal_sp("");
//			rn_fb_line_pk.setVal_sp_param("");
			rn_fb_line_pk.setSequence(false);
//			rn_fb_line_pk.setSeq_name("");
//			rn_fb_line_pk.setSeq_sp("");
//			rn_fb_line_pk.setSeq_sp_param("");
			rn_fb_line_pk.setDefault_1(false);
//			rn_fb_line_pk.setDefault_type("");
//			rn_fb_line_pk.setDefault_value("");
//			rn_fb_line_pk.setDefault_sp("");
//			rn_fb_line_pk.setDefault_sp_param("");
			rn_fb_line_pk.setCalculated_field(false);
//			rn_fb_line_pk.setCal_sp("");
//			rn_fb_line_pk.setCal_sp_param("");
//			rn_fb_line_pk.setAdd_to_grid("Y");
//			rn_fb_line_pk.setSp_for_autocomplete("N");
//			rn_fb_line_pk.setSp_for_dropdown("N");
			rn_fb_line_pk.setAdd_to_grid(true);
			rn_fb_line_pk.setSp_for_autocomplete(false);
			rn_fb_line_pk.setSp_for_dropdown(false);
//			rn_fb_line_pk.setSp_name_for_dropdown("");
//			rn_fb_line_pk.setSp_name_for_autocomplete("");
//			rn_fb_line_pk.setLine_table_name("");
			rn_fb_line_pk.setLine_table_no(0);
			// WHO
			rn_fb_line_pk.setCreatedBy(userId);
			rn_fb_line_pk.setUpdatedBy(userId);
//			rn_fb_line_pk.setAccountId(accountId);
			rn_fb_line_pk.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_pk);

			/**
			 * Header TextField in MultiLine
			 */
			for (int i = 1; i <= 4; i++) {
				Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();
				rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + i);
				rn_fb_line_textfield.setSeq(i);
				rn_fb_line_textfield.setFieldName(WireFrameConstant.LABEL + i);
				rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
				rn_fb_line_textfield.setSection_num(0);
				rn_fb_line_textfield.setButton_num(0);
				rn_fb_line_textfield.setForm_type(formType);
				rn_fb_line_textfield.setType1(formType);
				rn_fb_line_textfield.setType2(WireFrameConstant.HEADER_TYPE);
				rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
				rn_fb_line_textfield.setFormCode(formCode);
				rn_fb_line_textfield.setKey1("");
//				rn_fb_line_textfield.setMandatory("N");
//				rn_fb_line_textfield.setHidden("N");
//				rn_fb_line_textfield.setReadonly("N");
//				rn_fb_line_textfield.setDependent("N");
				rn_fb_line_textfield.setMandatory(false);
				rn_fb_line_textfield.setHidden(false);
				rn_fb_line_textfield.setReadonly(false);
				rn_fb_line_textfield.setDependent(false);
//				rn_fb_line_textfield.setDependent_on("");
//				rn_fb_line_textfield.setDependent_sp("");
//				rn_fb_line_textfield.setDependent_sp_param("");
				rn_fb_line_textfield.setValidation_1(false);
//				rn_fb_line_textfield.setVal_type("");
//				rn_fb_line_textfield.setVal_sp("");
//				rn_fb_line_textfield.setVal_sp_param("");
				rn_fb_line_textfield.setSequence(false);
//				rn_fb_line_textfield.setSeq_name("");
//				rn_fb_line_textfield.setSeq_sp("");
//				rn_fb_line_textfield.setSeq_sp_param("");
				rn_fb_line_textfield.setDefault_1(false);
//				rn_fb_line_textfield.setDefault_type("");
//				rn_fb_line_textfield.setDefault_value("");
//				rn_fb_line_textfield.setDefault_sp("");
//				rn_fb_line_textfield.setDefault_sp_param("");
				rn_fb_line_textfield.setCalculated_field(false);
//				rn_fb_line_textfield.setCal_sp("");
//				rn_fb_line_textfield.setCal_sp_param("");
//				rn_fb_line_textfield.setAdd_to_grid("Y");
//				rn_fb_line_textfield.setSp_for_autocomplete("N");
//				rn_fb_line_textfield.setSp_for_dropdown("N");
				rn_fb_line_textfield.setAdd_to_grid(true);
				rn_fb_line_textfield.setSp_for_autocomplete(false);
				rn_fb_line_textfield.setSp_for_dropdown(false);
//				rn_fb_line_textfield.setSp_name_for_dropdown("");
//				rn_fb_line_textfield.setSp_name_for_autocomplete("");
//				rn_fb_line_textfield.setLine_table_name("");
				rn_fb_line_textfield.setLine_table_no(0);
				// WHO
				rn_fb_line_textfield.setCreatedBy(userId);
				rn_fb_line_textfield.setUpdatedBy(userId);
//				rn_fb_line_textfield.setAccountId(accountId);
				rn_fb_line_textfield.setRn_fb_header(savedHeader);
				this.saveLine(rn_fb_line_textfield);

			}

			/**
			 * Header Part Button values in MultiLine WireFrame
			 *
			 */
			Rn_Fb_Line rn_fb_line_button = new Rn_Fb_Line();
//			rn_fb_line_button.setHeader_id(header_id);
//			rn_fb_line_button.setProject_id(project_id);
//			rn_fb_line_button.setModule_id(module_id);
//			rn_fb_line_button.setAccount_id(0);

			rn_fb_line_button.setMapping(WireFrameConstant.BUTTON);
			rn_fb_line_button.setSeq(0);

			rn_fb_line_button.setFieldName("submit");
			rn_fb_line_button.setType_field(WireFrameConstant.BUTTON);
			rn_fb_line_button.setSection_num(0);
			rn_fb_line_button.setButton_num(0);

			rn_fb_line_button.setForm_type(formType);
			rn_fb_line_button.setType1(formType);
			rn_fb_line_button.setType2(WireFrameConstant.LINE_TYPE);
			rn_fb_line_button.setDataType("N");
			rn_fb_line_button.setFormCode(formCode);
			rn_fb_line_button.setKey1("");

//			rn_fb_line_button.setMandatory("N");
//			rn_fb_line_button.setHidden("N");
//			rn_fb_line_button.setReadonly("N");
//			rn_fb_line_button.setDependent("N");
			rn_fb_line_button.setMandatory(false);
			rn_fb_line_button.setHidden(false);
			rn_fb_line_button.setReadonly(false);
			rn_fb_line_button.setDependent(false);

//			rn_fb_line_button.setDependent_on("");
//			rn_fb_line_button.setDependent_sp("");
//			rn_fb_line_button.setDependent_sp_param("");

			rn_fb_line_button.setValidation_1(false);

//			rn_fb_line_button.setVal_type("");
//			rn_fb_line_button.setVal_sp("");
//			rn_fb_line_button.setVal_sp_param("");

			rn_fb_line_button.setSequence(false);

//			rn_fb_line_button.setSeq_name("");
//			rn_fb_line_button.setSeq_sp("");
//			rn_fb_line_button.setSeq_sp_param("");

			rn_fb_line_button.setDefault_1(false);

//			rn_fb_line_button.setDefault_type("");
//			rn_fb_line_button.setDefault_value("");
//			rn_fb_line_button.setDefault_sp("");
//			rn_fb_line_button.setDefault_sp_param("");

			rn_fb_line_button.setCalculated_field(false);
			rn_fb_line_button.setCal_sp("");
			rn_fb_line_button.setCal_sp_param("");

//			rn_fb_line_button.setAdd_to_grid("Y");
//			rn_fb_line_button.setSp_for_autocomplete("N");
//			rn_fb_line_button.setSp_for_dropdown("N");
			rn_fb_line_button.setAdd_to_grid(true);
			rn_fb_line_button.setSp_for_autocomplete(false);
			rn_fb_line_button.setSp_for_dropdown(false);

//			rn_fb_line_button.setSp_name_for_dropdown("");
//			rn_fb_line_button.setSp_name_for_autocomplete("");
//			rn_fb_line_button.setLine_table_name("");
			rn_fb_line_button.setLine_table_no(0);

			// WHO
			rn_fb_line_button.setCreatedBy(userId);
			rn_fb_line_button.setUpdatedBy(userId);
//			rn_fb_line_button.setAccountId(accountId);
			rn_fb_line_button.setRn_fb_header(savedHeader);
			this.saveLine(rn_fb_line_button);
		}
		return true;
	}

//	@Override
//	public boolean editWireframe(int id, Rn_Fb_Header Rn_Fb_Header) {
//		return false;
//	}

	@Override
	public boolean addNewSectionOrButton(int rn_fb_header_id, AddSectionButtonDTO addSectionButtonDTO) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();

		Rn_Fb_Header rn_fb_header = this.getById(rn_fb_header_id);
		List<Rn_Fb_Line> rn_fb_lines = rn_fb_header.getRn_fb_lines();

		String formCode = rn_fb_header.getFormCode();
		String formType = rn_fb_header.getFormType();

		String type1 = rn_fb_lines.get(0).getType1();
		String type2 = rn_fb_lines.get(0).getType2();

		String type = addSectionButtonDTO.getType();
		log.debug("Section Or Button Type : {} ", type);
		if (WireFrameConstant.SECTION.equalsIgnoreCase(type)) {
			int sectionNumber = fbLineRepository.findMaxSectionNumber(rn_fb_header_id);
			log.debug("Max Section number : {}", sectionNumber);
			// int increment = ++sectionNumber;
			int increment = sectionNumber + 1;
			Rn_Fb_Line rn_fb_line_section = new Rn_Fb_Line();
			rn_fb_line_section.setFieldName("section" + increment);
			rn_fb_line_section.setMapping("section" + increment);
			rn_fb_line_section.setDataType("N");
			rn_fb_line_section.setType_field("section");
			rn_fb_line_section.setFormCode(formCode);
			rn_fb_line_section.setKey1("");
			rn_fb_line_section.setForm_type(formType);
			rn_fb_line_section.setType1(type1);
			rn_fb_line_section.setType2(type2);
			rn_fb_line_section.setSeq(0);
			rn_fb_line_section.setSection_num(increment);
			rn_fb_line_section.setButton_num(0);
//			rn_fb_line.setMandatory("N");
//			rn_fb_line.setHidden("N");
//			rn_fb_line.setReadonly("N");
//			rn_fb_line.setDependent("N");
			rn_fb_line_section.setMandatory(false);
			rn_fb_line_section.setHidden(false);
			rn_fb_line_section.setReadonly(false);
			rn_fb_line_section.setDependent(false);
//			rn_fb_line_section.setDependent_on("");
//			rn_fb_line.setDependent_sp("");
//			rn_fb_line.setDependent_sp_param("");
			rn_fb_line_section.setValidation_1(false);
//			rn_fb_line.setVal_type("");
//			rn_fb_line.setVal_sp("");
//			rn_fb_line.setVal_sp_param("");
			rn_fb_line_section.setSequence(false);
//			rn_fb_line.setSeq_name("");
//			rn_fb_line.setSeq_sp("");
//			rn_fb_line.setSeq_sp_param("");
			rn_fb_line_section.setDefault_1(false);
//			rn_fb_line.setDefault_type("");
//			rn_fb_line.setDefault_value("");
//			rn_fb_line.setDefault_sp("");
//			rn_fb_line.setDefault_sp_param("");
			rn_fb_line_section.setCalculated_field(false);
//			rn_fb_line.setCal_sp("");
//			rn_fb_line.setCal_sp_param("");
//			rn_fb_line.setSp_name_for_autocomplete("");
			// rn_fb_line_section.setAdd_to_grid("Y");
			// rn_fb_line_section.setSp_for_autocomplete("N");
			// rn_fb_line_section.setSp_for_dropdown("N");
			rn_fb_line_section.setAdd_to_grid(true);
			rn_fb_line_section.setSp_for_autocomplete(false);
			rn_fb_line_section.setSp_for_dropdown(false);
//			rn_fb_line_section.setSp_name_for_dropdown("");

			// WHO
			rn_fb_line_section.setCreatedBy(userId);
			rn_fb_line_section.setUpdatedBy(userId);
			rn_fb_line_section.setRn_fb_header(rn_fb_header);
			this.saveLine(rn_fb_line_section);

			int seqNumber = fbLineRepository.findMaxSeqOfFields(rn_fb_header_id);
			log.debug("Max SEQ number : {}", seqNumber);

			// add fields in section
			int temp1 = 3;
			if (temp1 > 0 && temp1 < 4) {
				// add 4 line fields with default value (textfield1,2,3,4)
				for (int i = 1; i <= 4; i++) {
					System.out.println("for loop i = " + i);
					// seqNumber = seqNumber + 1;
					seqNumber += 1;
					System.out.println("sequence : " + seqNumber);
					Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();
					rn_fb_line_textfield.setFieldName(WireFrameConstant.LABEL + seqNumber);
					rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + seqNumber);
					rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
					rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
					rn_fb_line_textfield.setFormCode(formCode);
					rn_fb_line_textfield.setKey1("");

					rn_fb_line_textfield.setForm_type(formType);
					rn_fb_line_textfield.setType1(type1);
					rn_fb_line_textfield.setType2(type2);

					rn_fb_line_textfield.setSeq(seqNumber);
					rn_fb_line_textfield.setSection_num(increment);
					rn_fb_line_textfield.setButton_num(0);
//					rn_fb_line_textfield.setMandatory("N");
//					rn_fb_line_textfield.setHidden("N");
//					rn_fb_line_textfield.setReadonly("N");
//					rn_fb_line_textfield.setDependent("N");
					rn_fb_line_textfield.setMandatory(false);
					rn_fb_line_textfield.setHidden(false);
					rn_fb_line_textfield.setReadonly(false);
					rn_fb_line_textfield.setDependent(false);
//					rn_fb_line_textfield.setDependent_on("");
//					rn_fb_line_textfield.setDependent_sp("");
//					rn_fb_line_textfield.setDependent_sp_param("");
					rn_fb_line_textfield.setValidation_1(false);
//					rn_fb_line_textfield.setVal_type("");
//					rn_fb_line_textfield.setVal_sp("");
//					rn_fb_line_textfield.setVal_sp_param("");
					rn_fb_line_textfield.setSequence(false);
//					rn_fb_line_textfield.setSeq_name("");
//					rn_fb_line_textfield.setSeq_sp("");
//					rn_fb_line_textfield.setSeq_sp_param("");
					rn_fb_line_textfield.setDefault_1(false);
//					rn_fb_line_textfield.setDefault_type("");
//					rn_fb_line_textfield.setDefault_value("");
//					rn_fb_line_textfield.setDefault_sp("");
//					rn_fb_line_textfield.setDefault_sp_param("");
					rn_fb_line_textfield.setCalculated_field(false);
//					rn_fb_line_textfield.setCal_sp("");
//					rn_fb_line_textfield.setCal_sp_param("");
//					rn_fb_line_textfield.setSp_name_for_autocomplete("");
//					rn_fb_line_textfield.setAdd_to_grid("Y");
//					rn_fb_line_textfield.setSp_for_autocomplete("N");
//					rn_fb_line_textfield.setSp_for_dropdown("N");
//					rn_fb_line_textfield.setSp_name_for_dropdown("");

					// WHO
					rn_fb_line_textfield.setCreatedBy(userId);
					rn_fb_line_textfield.setUpdatedBy(userId);
					rn_fb_line_textfield.setRn_fb_header(rn_fb_header);
					this.saveLine(rn_fb_line_textfield);
					// added 4 default fields in section end in section condition of default 4
					// values
				}
			}
		}
		// if user added a extra button
		else if (WireFrameConstant.BUTTON.equalsIgnoreCase(type)) {

			int maxButtonNum = fbLineRepository.findMaxButtonNumber(rn_fb_header_id);
			log.debug("Max BUTTON number : {}", maxButtonNum);
			int increment = 0;
			increment = maxButtonNum + 1;

			Rn_Fb_Line rn_fb_line_button = new Rn_Fb_Line();
			rn_fb_line_button.setFieldName(WireFrameConstant.BUTTON + increment);
			rn_fb_line_button.setMapping(WireFrameConstant.BUTTON + increment);
			rn_fb_line_button.setDataType("N");
			rn_fb_line_button.setType_field(WireFrameConstant.BUTTON);
			rn_fb_line_button.setMethodName(WireFrameConstant.BUTTON + increment + "_method");
			rn_fb_line_button.setFormCode(formCode);
			rn_fb_line_button.setKey1("");
			rn_fb_line_button.setForm_type(formType);
			rn_fb_line_button.setType1(type1);
			rn_fb_line_button.setType2(type2);
			rn_fb_line_button.setSeq(0);
			rn_fb_line_button.setSection_num(0);
			rn_fb_line_button.setButton_num(increment);
			rn_fb_line_button.setMandatory(false);
			rn_fb_line_button.setHidden(false);
			rn_fb_line_button.setReadonly(false);
			rn_fb_line_button.setDependent(false);
			rn_fb_line_button.setValidation_1(false);
			rn_fb_line_button.setSequence(false);
			rn_fb_line_button.setDefault_1(false);
			rn_fb_line_button.setCalculated_field(false);
			rn_fb_line_button.setAdd_to_grid(true);
			rn_fb_line_button.setSp_for_autocomplete(false);
			rn_fb_line_button.setSp_for_dropdown(false);
//			rn_fb_line_button.setSp_name_for_dropdown("");
			// WHO
			rn_fb_line_button.setCreatedBy(userId);
			rn_fb_line_button.setUpdatedBy(userId);
			rn_fb_line_button.setRn_fb_header(rn_fb_header);
			this.saveLine(rn_fb_line_button);
		}
		return true;

	}

	// only in header section( used in all kind of form)
	@Override
	public boolean addNewFieldInSection(int id, int sectionNum) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();

		Rn_Fb_Header rn_fb_header = this.getById(id);
		List<Rn_Fb_Line> rn_fb_lines = rn_fb_header.getRn_fb_lines();
		String formCode = rn_fb_header.getFormCode();
		String formType = rn_fb_header.getFormType();
		String type1 = rn_fb_lines.get(0).getType1();
		String type2 = rn_fb_lines.get(0).getType2();

		int maxSeq = fbLineRepository.findMaxSeqOfFields(id);
		log.debug("Max SEQ number : {}", maxSeq);
		maxSeq += 1;

		Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();
		rn_fb_line_textfield.setFieldName(WireFrameConstant.LABEL + maxSeq);
		rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + maxSeq);
		rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
		rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
		rn_fb_line_textfield.setFormCode(formCode);
		rn_fb_line_textfield.setKey1("");
		rn_fb_line_textfield.setType1(formType);
		rn_fb_line_textfield.setType2(WireFrameConstant.HEADER_TYPE);
		rn_fb_line_textfield.setForm_type(formType);
		rn_fb_line_textfield.setSeq(maxSeq);
		rn_fb_line_textfield.setSection_num(sectionNum);
		rn_fb_line_textfield.setButton_num(0);
		rn_fb_line_textfield.setMandatory(false);
		rn_fb_line_textfield.setHidden(false);
		rn_fb_line_textfield.setReadonly(false);
		rn_fb_line_textfield.setDependent(false);
//		rn_fb_line.setDependent_on("");
//		rn_fb_line.setDependent_sp("");
//		rn_fb_line.setDependent_sp_param("");
		rn_fb_line_textfield.setValidation_1(false);
//		rn_fb_line_textfield.setVal_type("");
//		rn_fb_line_textfield.setVal_sp("");
//		rn_fb_line_textfield.setVal_sp_param("");
		rn_fb_line_textfield.setSequence(false);
//		rn_fb_line_textfield.setSeq_name("");
//		rn_fb_line_textfield.setSeq_sp("");
//		rn_fb_line_textfield.setSeq_sp_param("");
		rn_fb_line_textfield.setDefault_1(false);
//		rn_fb_line_textfield.setDefault_type("");
//		rn_fb_line_textfield.setDefault_value("");
//		rn_fb_line_textfield.setDefault_sp("");
//		rn_fb_line_textfield.setDefault_sp_param("");
		rn_fb_line_textfield.setCalculated_field(false);
//		rn_fb_line_textfield.setCal_sp("");
//		rn_fb_line_textfield.setCal_sp_param("");
//		rn_fb_line_textfield.setSp_name_for_autocomplete("");
//		rn_fb_line_textfield.setAdd_to_grid("Y");
//		rn_fb_line_textfield.setSp_for_autocomplete("N");
//		rn_fb_line_textfield.setSp_for_dropdown("N");
		rn_fb_line_textfield.setAdd_to_grid(true);
		rn_fb_line_textfield.setSp_for_autocomplete(false);
		rn_fb_line_textfield.setSp_for_dropdown(false);
//		rn_fb_line_textfield.setSp_name_for_dropdown("");
		// WHO
		rn_fb_line_textfield.setCreatedBy(userId);
		rn_fb_line_textfield.setUpdatedBy(userId);
		rn_fb_line_textfield.setRn_fb_header(rn_fb_header);
		
		
		this.saveLine(rn_fb_line_textfield);
		
		
		return true;
	}
	
	
	// used in HL & LO form (FOR ADDING LINE FIELDS)
	@Override
	public boolean addNewFieldInOnlyLineSection(int id, int sectionNumber) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();

		Rn_Fb_Header rn_fb_header = this.getById(id);
		List<Rn_Fb_Line> rn_fb_lines = rn_fb_header.getRn_fb_lines();

		String formCode = rn_fb_header.getFormCode();
		String formType = rn_fb_header.getFormType();

		String type1 = rn_fb_lines.get(0).getType1();
		String type2 = rn_fb_lines.get(0).getType2();

		
		// alternative use : findMaxSeqOfFields() 
		// pass fb header id and section number and form type
		int maxSeq = fbLineRepository.findMaxSeqWithFormTypeAndSectionNumber(id, sectionNumber, formType);
		log.debug("Max SEQ number : {}", maxSeq);
		maxSeq += 1;

		Rn_Fb_Line rn_fb_line_textfield = new Rn_Fb_Line();
		rn_fb_line_textfield.setFieldName(WireFrameConstant.COLUMN + maxSeq);
		rn_fb_line_textfield.setMapping(WireFrameConstant.DT_TEXTFIELD + maxSeq);
		rn_fb_line_textfield.setDataType(WireFrameConstant.DT_VARCHAR);
		rn_fb_line_textfield.setType_field(WireFrameConstant.DT_TEXTFIELD);
		rn_fb_line_textfield.setFormCode(formCode);
		rn_fb_line_textfield.setType1(formType);
		rn_fb_line_textfield.setKey1("");
		rn_fb_line_textfield.setType2(WireFrameConstant.LINE_TYPE);
		rn_fb_line_textfield.setForm_type(formType);
		rn_fb_line_textfield.setSeq(maxSeq);
		rn_fb_line_textfield.setSection_num(sectionNumber);
		rn_fb_line_textfield.setButton_num(0);
		rn_fb_line_textfield.setMandatory(false);
		rn_fb_line_textfield.setHidden(false);
		rn_fb_line_textfield.setReadonly(false);
		rn_fb_line_textfield.setDependent(false);
		rn_fb_line_textfield.setValidation_1(false);
		rn_fb_line_textfield.setSequence(false);
		rn_fb_line_textfield.setDefault_1(false);
		rn_fb_line_textfield.setCalculated_field(false);
		rn_fb_line_textfield.setAdd_to_grid(true);
		rn_fb_line_textfield.setSp_for_autocomplete(false);
		rn_fb_line_textfield.setSp_for_dropdown(false);
		// WHO
		rn_fb_line_textfield.setCreatedBy(userId);
		rn_fb_line_textfield.setUpdatedBy(userId);
		rn_fb_line_textfield.setRn_fb_header(rn_fb_header);
		
		// save object
		Rn_Fb_Line savedLine = this.saveLine(rn_fb_line_textfield);
		
		if(savedLine == null) {
			return false;
		} else {
			return true;
		}
	}

	// need modification
	@Override
	@JsonIgnore
	@JsonProperty(value = "createdAt")
	public boolean updateLine(int lineId, Rn_Fb_Line_DTO line) {
//		in update properties wireframe
		Rn_Fb_Line oldLine = fbLineRepository.findById(lineId)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + lineId));
		oldLine.setFieldName(line.getFieldName());
		oldLine.setMapping(line.getMapping());
		oldLine.setDataType(line.getDataType());
		oldLine.setFormCode(line.getFormCode());
		oldLine.setKey1(line.getKey1());
		oldLine.setType1(line.getType1());
		oldLine.setMandatory(line.isMandatory());
		oldLine.setHidden(line.isHidden());
		oldLine.setReadonly(line.isReadonly());
		oldLine.setDependent(line.isDependent());
		oldLine.setDependent_on(line.getDependent_on());
		oldLine.setDependent_sp(line.getDependent_sp());
		oldLine.setDependent_sp_param(line.getDependent_sp_param());
		oldLine.setValidation_1(line.isValidation_1());
		oldLine.setVal_type(line.getVal_type());
		oldLine.setVal_sp(line.getVal_sp());
		oldLine.setVal_sp_param(line.getVal_sp_param());
		oldLine.setSequence(line.isSequence());
		oldLine.setSeq_name(line.getSeq_name());
		oldLine.setSeq_sp(line.getSeq_sp());
		oldLine.setSeq_sp_param(line.getSeq_sp_param());
		oldLine.setDefault_1(line.isDefault_1());
		oldLine.setDefault_type(line.getDefault_type());
		oldLine.setDefault_value(line.getDefault_value());
		oldLine.setDefault_sp(line.getDefault_sp());
		oldLine.setDefault_sp_param(line.getDefault_sp_param());
		oldLine.setCalculated_field(line.isCalculated_field());
		oldLine.setCal_sp(line.getCal_sp());
		oldLine.setCal_sp_param(line.getCal_sp_param());
		oldLine.setAdd_to_grid(line.isAdd_to_grid());
		oldLine.setSp_for_autocomplete(line.isSp_for_autocomplete());
		oldLine.setSp_for_dropdown(line.isSp_for_dropdown());
		oldLine.setSp_name_for_dropdown(line.getSp_name_for_dropdown());
		oldLine.setSp_name_for_autocomplete(line.getSp_name_for_autocomplete());
		oldLine.setType_field(line.getType_field());
		oldLine.setMethodName(line.getMethodName());
		oldLine.setSeq(line.getSeq());
		oldLine.setForm_type(line.getForm_type());
		oldLine.setSection_num(line.getSection_num());
		oldLine.setButton_num(line.getButton_num());
		oldLine.setType2(line.getType2());
		oldLine.setLine_table_name(line.getLine_table_name());
		oldLine.setLine_table_no(line.getLine_table_no());
		oldLine.setAction(line.getAction());
		oldLine.setAction_uiname(line.getAction_uiname());
		oldLine.setRequest_param(line.getRequest_param());
		
		// oldLine.setTable_name
		final Rn_Fb_Line updatedLine = this.saveLine(oldLine);
		if (updatedLine == null) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public List<Rn_Fb_Line> getButtonList(int header_id) {
		return fbLineRepository.findButtonList(header_id);
	}

	@Override
	public List<Rn_Fb_Line> getExtraButton(int header_id) {
		return fbLineRepository.findExtraButton(header_id);
	}

	@Override
	public List<DropDownDTO> getFbHeadersForDropDown() {
		//List<Rn_Fb_Header> fbHeader = fbHeaderRepository.findFbHeadersForDropDown();
		List<Rn_Fb_Header> fbHeader = this.getAll();
		List<DropDownDTO> dto = new ArrayList<DropDownDTO>();
		for (Rn_Fb_Header header : fbHeader) {
			DropDownDTO dt = new DropDownDTO();
			dt.setId(header.getId());
			dt.setName(header.getUiName());
			dto.add(dt);
		}
//		//List<WFDropDownDTO> wfDTO = modelMapper.map(fbHeader, WFDropDownDTO.class);
//		List<WFDropDownDTO> dtos = fbHeader
//				  .stream()
//				  .map(header -> modelMapper.map(header, WFDropDownDTO.class))
//				  .collect(Collectors.toList());
//		//return fbHeaderRepository.findFbHeadersForDropDown();
//		return dtos;
		return dto;
	}
	
	// ----------------- data type fields -------------- //

	@Override
	public List<DropDownDTO> getModuleWireframesForDropDown(int moduleId) {
		List<Rn_Fb_Header> fbHeader = fbHeaderRepository.findModuleWireframesForDropDown(moduleId);
		List<DropDownDTO> dto = new ArrayList<DropDownDTO>();
		for (Rn_Fb_Header header : fbHeader) {
			DropDownDTO dt = new DropDownDTO();
			dt.setId(header.getId());
			dt.setName(header.getUiName());
			dto.add(dt);
		}

		return dto;
	}
	
	
	@Override
	public List<Rn_Fb_Line> getSection(int id) {
		return fbLineRepository.findSection(id);
	}
	
	@Override
	public List<Rn_Fb_Line> getSectionFields(int section_number, int id) {
		return fbLineRepository.findSectionFields(section_number, id);
	}
	
	@Override
	public List<Rn_Fb_Line> getSectionFieldsForFrontEnd(int section_number, int header_id) {
		return fbLineRepository.findSectionFieldsForFrontEnd(section_number, header_id);
	}
	
	@Override
	public List<Rn_Fb_Line> getPrimaryKeyField(int id) {
		return fbLineRepository.findPrimaryKeyField(id);
	}
	
	@Override
	public List<Rn_Fb_Line> getIntegerFields(int id) {
		return fbLineRepository.findIntegerFields(id);
	}
	
	@Override
	public List<Rn_Fb_Line> getVarcharFields(int id) {
		return fbLineRepository.findVarcharFields(id);
	}

	@Override
	public List<Rn_Fb_Line> getDateTimeFields(int id) {
		return fbLineRepository.findDateTimeFields(id);
	}

	@Override
	public List<Rn_Fb_Line> getLongtextFields(int id) {
		return fbLineRepository.findLongtextFields(id);
	}

	@Override
	public List<Rn_Fb_Line> getDoubleFields(int id) {
		return fbLineRepository.findDoubleFields(id);
	}

	//======== H-L ========//
	@Override
	public List<Rn_Fb_Line> getLinePrimarkKeyField(int id) {
		return fbLineRepository.findLinePrimarkKeyField(id);
	}

	@Override
	public List<Rn_Fb_Line> getLineSection(int id) {
		return fbLineRepository.findLineSection(id); // used in HL form
	}
	
	@Override
	public List<Rn_Fb_Line> getLineSectionFieldsForFrontEnd(int header_id) {
		return fbLineRepository.findLineSectionFieldsForFrontEnd(header_id);
	}
	
	

	@Override
	public List<Rn_Fb_Line> getLineVarcharFields(int id) {
		return fbLineRepository.findLineVarcharFields(id);
	}
	@Override
	public List<Rn_Fb_Line> getLineIntegerFields(int id) {
		return fbLineRepository.findLineIntegerFields(id);
	}
	
	// ============ O-L ============= /
	@Override
	public List<Rn_Fb_Line> getOnlyLineSectionFields(int id, int sectionNumber){
		return fbLineRepository.findOnlyLineSectionFields(sectionNumber, id);
		
	}
	// ============ H-L ============/
	
	@Override
	public List<Rn_Fb_Line> getHeaderFields(int id) {
		return fbLineRepository.findHeaderFields(id);
	}
	@Override
	public List<Rn_Fb_Line> getLineFields(int id) {
		return fbLineRepository.findLineFields(id);
	}
	
	// MULTI-LINE
	@Override
	public List<Rn_Fb_Line> getHidIntegerFields(int id) {
		return fbLineRepository.findHidIntegerFields(id);
	}
	
	// delete section
	@Override
	public boolean deleteSection(int headerId, int sectionNumber) {
		List<Rn_Fb_Line> fb_lines = fbLineRepository.deleteSection(headerId, sectionNumber);
		if(fb_lines.isEmpty()) {
			throw new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION);
		}
		for(Rn_Fb_Line line : fb_lines) {
			fbLineRepository.delete(line);
		}
		return true;
	}

	@Override
	public Rn_Fb_Header uinamechange(int id,Rn_Fb_HeaderDTO uiname) {
		Rn_Fb_Header oldLine = fbHeaderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		oldLine.setUiName(uiname.getUiName());
		
		final Rn_Fb_Header updatedLine = this.save(oldLine);
		return updatedLine;
	}
}
