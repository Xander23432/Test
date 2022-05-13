package com.realnet.wfb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.entity.WireFrameCopyDTO;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.fnd.service.Rn_ModuleSetup_Service;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;
import com.realnet.utils.WireFrameConstant;
import com.realnet.wfb.entity.AddSectionButtonDTO;
import com.realnet.wfb.entity.Button;
import com.realnet.wfb.entity.Field;
import com.realnet.wfb.entity.Header;
import com.realnet.wfb.entity.Line;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_HeaderDTO;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.entity.Rn_Fb_Line_DTO;
import com.realnet.wfb.entity.Rn_WireFrameDTO;
import com.realnet.wfb.entity.Section;
import com.realnet.wfb.entity.WireFrameEditDTO;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "WireFrame" })
public class Rn_WireFrameController {

	@Autowired
	private AppUserServiceImpl userService;

	@Autowired
	private Rn_ModuleSetup_Service moduleService;
	@Autowired
	private Rn_WireFrame_Service wireframeService;

	@Value("${projectPath}")
	private String projectPath;

	/*
	 * This is using pageImpl class please note down that will be useful to sort
	 * list of data
	 */

	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Wireframes", response = CustomResponse.class)
	@GetMapping("/wireframe")
	public CustomResponse getWireframes(@RequestParam(value = "moduleId") Integer moduleId,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		Rn_Module_Setup module = moduleService.getById(moduleId);
		List<Rn_Fb_Header> headers = module.getRn_fb_headers();
//		log.debug("Rn_Fb_Header -> {}", headers);
//		log.debug("Rn_Module_Setup -> {}", headers.get(0).getModule());
//		log.debug("Rn_Project_Setup -> {}", headers.get(0).getModule().getProject());

		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_Fb_Header> result = new PageImpl<>(headers, paging, headers.size());
		// Page<Rn_Fb_Header> result = wireframeService.getAll(paging);
		// log.debug("SORTED FB_HEADERS : {}", result.getContent());

		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// GET BY ID
	@ApiOperation(value = "Get A Wireframe", response = Rn_Fb_Header.class)
	@GetMapping("/wireframe/{id}")
	public ResponseEntity<?> getWireframeDetails(@PathVariable(value = "id") int id) {
		Rn_Fb_Header form = wireframeService.getById(id);
		// Map<String, Rn_Project_Setup> extractorMap =
		// Collections.singletonMap("extractior", bcf_extractor);
		// return new ResponseEntity<Map<String, Rn_Project_Setup>>(extractorMap,
		// HttpStatus.OK);
		// return ResponseEntity.ok().body(bcf_extractor);
		return new ResponseEntity<Rn_Fb_Header>(form, HttpStatus.OK);
	}

	// ==== NEED MODIFICATION DEPENDING ON FORM-TYPE =======//
	// MULTILINE FORM IS PENDING
	// GET ALL RN_FB LINES (SECTION AND FIELDS FOR UI)
	
	@ApiOperation(value = "List of Wireframes", response = Section.class)
	@GetMapping("/wireframe-lines/{id}")
	public ResponseEntity<?> getLineWireframe(@PathVariable(value = "id") int id) { // rn_gb header id

		System.out.println("calling wireframe get line controller");

		Rn_Fb_Header fb_header = wireframeService.getById(id);
		String formType = fb_header.getFormType();

		log.debug("formType : {}", formType);

		// if form type : header_only
		if (WireFrameConstant.HEADER_ONLY.equalsIgnoreCase(formType)) 
		{
			List<Section> sectionList = new ArrayList<Section>();
			
			List<Button> buttonlist =new ArrayList<Button>();
			
			List<Rn_Fb_Line> sections = wireframeService.getSection(id);
				
			List<Rn_Fb_Line> extraButton = wireframeService.getExtraButton(id);
			
			//section list
			
			for (Rn_Fb_Line section : sections) {
				List<Field> fieldList = new ArrayList<Field>();
				
				int secId = section.getId();
				String secFieldName = section.getFieldName();
				int section_number = section.getSection_num();

				Section sectionObj = new Section();
				sectionObj.setId(secId);
				sectionObj.setFieldName(secFieldName);
				sectionObj.setSection_num(section_number);
				System.out.println("section number = "+section_number);
				List<Rn_Fb_Line> fields = wireframeService.getSectionFieldsForFrontEnd(section_number, id);
//				System.out.println(" fields for section "+section_number+""+fields);
				
				
				for (Rn_Fb_Line field : fields) {
					if(field.getSection_num()==section_number)
					{ 
//						System.err.println("in if condition sec num "+section_number  );
					int fieldId = field.getId();
					String fieldName = field.getFieldName();
					String typeField = field.getType_field();
					int section_num = field.getSection_num();
					
					Field fieldObj = new Field();
					fieldObj.setId(fieldId);
					fieldObj.setFieldName(fieldName);
					fieldObj.setType_field(typeField);
					fieldObj.setSection_num(section_num);
					fieldList.add(fieldObj);
					
					
					}
					
				}
			
				sectionObj.setFields(fieldList);
				sectionList.add(sectionObj);
				
			}
			
			// button list
			
			for (Rn_Fb_Line  button: extraButton) {
				
				
				int bid = button.getId();
				String fieldName = button.getFieldName();
				String mapping = button.getMapping();
				String dataType = button.getDataType();
				String type_field = button.getType_field();
				
				Button buttonobj=new Button(bid,fieldName,mapping,dataType,type_field);

				buttonlist.add(buttonobj);
			
			}
			
			
			WireFrameEditDTO headerOnlyWireFrame = new WireFrameEditDTO();
			Header header = new Header();
			header.setSection(sectionList);
			header.setButton(buttonlist);
			headerOnlyWireFrame.setHeader(header);
			return new ResponseEntity<WireFrameEditDTO>(headerOnlyWireFrame, HttpStatus.OK);

		} else if (WireFrameConstant.HEADER_LINE.equalsIgnoreCase(formType)) 
		{
			// header part start
			List<Section> headerSectionList = new ArrayList<Section>();
			List<Button> buttonlist =new ArrayList<Button>();
			
			List<Rn_Fb_Line> headerSections = wireframeService.getSection(id);
			List<Rn_Fb_Line> extraButton = wireframeService.getExtraButton(id);
			
			for (Rn_Fb_Line section : headerSections) {
				List<Field> headerFieldList = new ArrayList<Field>();
				
				int headerSectionId = section.getId();
				String headerSectionFieldName = section.getFieldName();
				int headerSectionNumber = section.getSection_num();
				// log.debug("Section name : {}",headerSectionFieldName);
				// log.debug("section number {}", headerSectionNumber);
				log.debug("Section name : {}, section number: {}", headerSectionFieldName, headerSectionNumber);

				Section sectionObj = new Section();
				sectionObj.setId(headerSectionId);
				sectionObj.setFieldName(headerSectionFieldName);
				sectionObj.setSection_num(headerSectionNumber);
				List<Rn_Fb_Line> headerFields = wireframeService.getSectionFieldsForFrontEnd(headerSectionNumber, id);
				System.out.println("HeaderFields = " + headerFields);
				// log.debug("HeaderFields : {}", headerFields);
				for (Rn_Fb_Line field : headerFields) {
					int fieldId = field.getId();
					//log.debug("Header field Id : {}", fieldId);
					String fieldName = field.getFieldName();
					//log.debug("Header field name : {}", fieldName);
					String typeField = field.getType_field();
					int section_num =field.getSection_num();
					
					Field fieldObj = new Field();
					fieldObj.setId(fieldId);
					fieldObj.setFieldName(fieldName);
					fieldObj.setType_field(typeField);
					fieldObj.setSection_num(section_num);
					headerFieldList.add(fieldObj);
				}
				sectionObj.setFields(headerFieldList);
				headerSectionList.add(sectionObj);
			} // header part end
			
				for (Rn_Fb_Line  button: extraButton) {
				
				
				int bid = button.getId();
				String fieldName = button.getFieldName();
				String mapping = button.getMapping();
				String dataType = button.getDataType();
				String type_field = button.getType_field();
				
				Button buttonobj=new Button(bid,fieldName,mapping,dataType,type_field);

				buttonlist.add(buttonobj);
			
			}
			

			// line part start
			List<Section> lineSectionList = new ArrayList<Section>();
			List<Field> lineFieldList = new ArrayList<Field>();
			List<Rn_Fb_Line> lineSections = wireframeService.getLineSection(id);
			for (Rn_Fb_Line lineSection : lineSections) {
				int secId = lineSection.getId();
				String lineSectionFieldName = lineSection.getFieldName();
				int lineSectionNumber = lineSection.getSection_num();

				Section lineSectionObj = new Section();
				lineSectionObj.setId(secId);
				lineSectionObj.setFieldName(lineSectionFieldName);
				lineSectionObj.setSection_num(lineSectionNumber);
				List<Rn_Fb_Line> lineFields = wireframeService.getLineSectionFieldsForFrontEnd(id);
				
				for (Rn_Fb_Line lineField : lineFields) {
					int lineFieldId = lineField.getId();
					String lineFieldName = lineField.getFieldName();
					String lineTypeField = lineField.getType_field();
					int section_num=lineField.getSection_num();
					
					Field lineFieldObj = new Field();
					lineFieldObj.setId(lineFieldId);
					lineFieldObj.setFieldName(lineFieldName);
					lineFieldObj.setType_field(lineTypeField);
					lineFieldObj.setSection_num(section_num);
					lineFieldList.add(lineFieldObj);
				}
				lineSectionObj.setFields(lineFieldList);
				lineSectionList.add(lineSectionObj);
			} // line part end

			WireFrameEditDTO wireFrameDTO = new WireFrameEditDTO();
			Header header = new Header();
			header.setSection(headerSectionList);
			header.setButton(buttonlist);
			Line line = new Line();
			line.setSection(lineSectionList);
			wireFrameDTO.setHeader(header);
			wireFrameDTO.setLine(line);
			return new ResponseEntity<WireFrameEditDTO>(wireFrameDTO, HttpStatus.OK);
		} else if (WireFrameConstant.LINE_ONLY.equalsIgnoreCase(formType)) {
			/*
			 * Line only structure is like header only.
			 * */
			List<Section> lineSectionList = new ArrayList<Section>();
			List<Button> linebuttonlist =new ArrayList<Button>();
			List<Field> lineFieldList = new ArrayList<Field>();
			List<Rn_Fb_Line> lineSections = wireframeService.getSection(id);
			
			List<Rn_Fb_Line> lineextraButton = wireframeService.getExtraButton(id);
			System.out.println("line sections : "+lineSections);
			
			for (Rn_Fb_Line lineSection : lineSections) {
				int secId = lineSection.getId();
				String lineSectionFieldName = lineSection.getFieldName();
				
				int lineSectionNumber = lineSection.getSection_num();

				Section lineSectionObj = new Section();
				lineSectionObj.setId(secId);
				lineSectionObj.setFieldName(lineSectionFieldName);
				lineSectionObj.setSection_num(lineSectionNumber);
				List<Rn_Fb_Line> lineFields = wireframeService.getSectionFieldsForFrontEnd(lineSectionNumber, id);
				
				System.err.println(" line fields " +lineFields);
			
				for (Rn_Fb_Line lineField : lineFields) {
					int lineFieldId = lineField.getId();
					String lineFieldName = lineField.getFieldName();
					log.debug("Line field name : {}", lineFieldName);
					String lineTypeField = lineField.getType_field();
					int section_num=lineField.getSection_num();
					
					Field lineFieldObj = new Field();
					lineFieldObj.setId(lineFieldId);
					lineFieldObj.setFieldName(lineFieldName);
					lineFieldObj.setType_field(lineTypeField);
					lineFieldObj.setSection_num(section_num);
					
					lineFieldList.add(lineFieldObj);
				}
				
				lineFieldList.forEach(fl->{
					System.err.println("fields in line " +fl);
				});
				
				
				
				lineSectionObj.setFields(lineFieldList);
				lineSectionList.add(lineSectionObj);
			} 
			
				for (Rn_Fb_Line  button: lineextraButton) {
				
				
				int bid = button.getId();
				String fieldName = button.getFieldName();
				String mapping = button.getMapping();
				String dataType = button.getDataType();
				String type_field = button.getType_field();
				
				Button buttonobj=new Button(bid,fieldName,mapping,dataType,type_field);

				linebuttonlist.add(buttonobj);
			
			}
			
			// line part end

			WireFrameEditDTO onlyLineWireFrame = new WireFrameEditDTO();
			Line line = new Line();
			line.setSection(lineSectionList);
			line.setButton(linebuttonlist);
			onlyLineWireFrame.setLine(line);
			return new ResponseEntity<WireFrameEditDTO>(onlyLineWireFrame, HttpStatus.OK);
		} else if (WireFrameConstant.MULTILINE.equalsIgnoreCase(formType)) {
			/*
			 * @ML LOGIC IS PENDIG
			 * */
			
			// ===== WRITE LOGIC HERE ========
			WireFrameEditDTO multiLineWireFrame = new WireFrameEditDTO();
			Header header = new Header();
			Line line = new Line();
			multiLineWireFrame.setHeader(header);
			multiLineWireFrame.setLine(line);
			return new ResponseEntity<WireFrameEditDTO>(multiLineWireFrame, HttpStatus.OK);

		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.WIREFRAME_API_TITLE);
			error.setMessage(Constant.NOT_FOUND_EXCEPTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

	}

	// SAVE WAREFRAME
	@ApiOperation(value = "Add new Wireframe")
	@PostMapping(value = "/wireframe")
	public ResponseEntity<?> saveWireFrame(@Valid @RequestBody Rn_WireFrameDTO wireframeDTO,HttpSession session)
			throws IOException {
		
		
		System.out.println("Wireframe controller start");

		boolean status = wireframeService.saveWireframe(wireframeDTO, wireframeDTO.getFormType(), wireframeDTO.getModuleId());
		log.debug("final status {}", status);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.WIREFRAME_API_TITLE);
			error.setMessage(Constant.WIREFRAME_NOT_CREATED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// ======== RN_FB_LINE APIS
	@ApiOperation(value = "Get A Wireframe", response = Rn_Fb_Line.class)
	@GetMapping("/wireframe-line/{id}")
	public ResponseEntity<?> getWireframeLineDetails(@PathVariable(value = "id") int id) {
		Rn_Fb_Line form = wireframeService.getLineById(id);
		return new ResponseEntity<Rn_Fb_Line>(form, HttpStatus.OK);
	}

	// ------------ MANIPULATION OF DATA FOR RN_FB_LINES
	// ADD NEW SECTION OR BUTTON
	
	
	@ApiOperation(value = "Add new Section Or Button in Wireframe")
	@PostMapping(value = "/wireframe-add-section-button/{id}")
	public ResponseEntity<?> wireFrameAddSectionOrButton(@PathVariable(value = "id") Integer id,
			@Valid @RequestBody AddSectionButtonDTO addSectionButtonDTO) throws IOException {

		System.out.println("Caling add section controller");
		boolean status = wireframeService.addNewSectionOrButton(id, addSectionButtonDTO);
		log.debug("final status {}", status);

		String type = addSectionButtonDTO.getType();
		if (status == true) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			if (WireFrameConstant.SECTION.equalsIgnoreCase(type)) {
				success.setMessage(Constant.WIREFRAME_SECTION_ADDED);
			} else if (WireFrameConstant.BUTTON.equalsIgnoreCase(type)) {
				success.setMessage(Constant.WIREFRAME_BUTTON_ADDED);
			}
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.WIREFRAME_API_TITLE);
			error.setMessage(Constant.WIREFRAME_NOT_CREATED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// add new extra field in section
	@ApiOperation(value = "Add new Field in Wireframe Section")
	@GetMapping(value = "/wireframe-add-field-in-section")
	public ResponseEntity<?> wireFrameAddFieldsInSection(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "section") Integer sectionNum) throws IOException {

		boolean status = wireframeService.addNewFieldInSection(id, sectionNum);
		log.debug("final status {}", status);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_FIELD_ADDED_IN_SECTION);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.WIREFRAME_FIELD_NOT_ADDED_IN_SECTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// UPDATE SECTION NAME (need mod)
	@PostMapping(value = "/wireframe-update-field-name/{id}")
	public ResponseEntity<?> updateSection(@PathVariable("id") Integer id,
			@Valid @RequestBody Rn_Fb_Line_DTO rn_Fb_Line_DTO) throws IOException {
		// LINE VALUE
		Rn_Fb_Line rn_fb_line = wireframeService.getLineById(id);
		rn_fb_line.setFieldName(rn_Fb_Line_DTO.getFieldName());
		Rn_Fb_Line savedLine = wireframeService.saveLine(rn_fb_line);

		if (savedLine != null) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_FIELD_NAME_CHANGE_SUCCESS);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.ACCEPTED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.WIREFRAME_FIELD_NAME_CHANGE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

	}

	// SECTION DELETE
	@GetMapping(value = "/wireframe-delete-section/{header_id}")
	public ResponseEntity<?> deleteSection(@PathVariable("header_id") Integer headerId,
			@RequestParam("section_num") Integer sectionNumber) throws IOException { // this is rn_fb_header ID
		boolean status = wireframeService.deleteSection(headerId, sectionNumber);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_SECTION_DELETE_SUCCESS);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.WIREFRAME_SECTION_DELETE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

	}

	// DELETE LINE FIELDS(DATA TYPE FIELDS AND BUTTON) BY ID
	@GetMapping(value = "/wireframe-delete-field/{id}")
	public ResponseEntity<?> deleteField(@PathVariable("id") Integer id) throws IOException { // this is rn_fb_header ID

		boolean status = wireframeService.deleteLineById(id);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_FIELD_DELETE_SUCCESS);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.WIREFRAME_FIELD_DELETE_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

	}

	// -------- BASIC AND ADDITIONAL PROPERTIES UPDATE --------- //

	@PutMapping(value = "/wireframe-line/{id}")
	public ResponseEntity<?> basicAndAdditionalPropertiesUpdate(@PathVariable("id") Integer id,
			@Valid @RequestBody Rn_Fb_Line_DTO lineDTO) throws IOException { // this is rn_fb_header ID

		// There are two methods in service. (updateLineById , updateLine)
		// wireframeService.updateLineById(id, fb_line);
		System.out.println("Caling wireframe line cntroller");
		boolean status = wireframeService.updateLine(id, lineDTO);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_UPDATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.WIREFRAME_API_TITLE);
			error.setMessage(Constant.WIREFRAME_NOT_UPDATED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

	}
	
	@PutMapping(value = "/wireframe-uiname/{id}")
	public ResponseEntity<?> uinamechange(@PathVariable("id") Integer id,
			@Valid @RequestBody Rn_Fb_HeaderDTO uiname) throws IOException { // this is rn_fb_header ID
		System.out.println("Caling wireframe name update controller");
		System.out.println("uiname"+uiname);		
		Rn_Fb_Header uinamechange = wireframeService.uinamechange(id, uiname);
		
		if(uinamechange.getUiName()==null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("uiname null or not changed");
		}
		return ResponseEntity.status(HttpStatus.OK).body("uiname sucessfully changed");
		
		

	}

	// ADD FIELD IN LINE SECTION
	@ApiOperation(value = "Add new Field in Wireframe Section")
	@GetMapping(value = "/wireframe-add-field-in-line-section")
	public ResponseEntity<?> wireFrameAddFieldsInlINESection(@RequestParam(value = "id") Integer id,
			@RequestParam(value = "section") Integer sectionNum) throws IOException {

		boolean status = wireframeService.addNewFieldInOnlyLineSection(id, sectionNum);
		log.debug("field added in line section status : {}", status);

		if (status) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.WIREFRAME_API_TITLE);
			success.setMessage(Constant.WIREFRAME_FIELD_ADDED_IN_SECTION);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
			error.setMessage(Constant.WIREFRAME_FIELD_NOT_ADDED_IN_SECTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// // UPDATE HEADER
//	@ApiOperation(value = "Update A Project", response = Rn_Project_Setup.class)
//	@PutMapping("/wireframe/{id}")
//	public ResponseEntity<?> updateProject(
//			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
//			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_Fb_Header rn_fb_header) {
//		User loggedInUser = userService.getLoggedInUser();
//
//		// project.setUpdatedBy(loggedInUser.getUserId());
//
//		Rn_Fb_Header updatedProject = wireframeService.updateById(id, rn_fb_header);
//
//		if (rn_fb_header.getId() != rn_fb_header.getId()) {
//			ErrorPojo errorPojo = new ErrorPojo();
//			Error error = new Error();
//			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
//			error.setMessage(Constant.PROJECT_NOT_DELETED);
//			errorPojo.setError(error);
//			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
//		}
//
//		SuccessPojo successPojo = new SuccessPojo();
//		Success success = new Success();
//		success.setTitle(Constant.EXTRACTOR_API_TITLE);
//		success.setMessage(Constant.PROJECT__UPDATED_SUCCESSFULLY);
//		successPojo.setSuccess(success);
//		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
//	}
//
//	// DELETE
//	@DeleteMapping("/wireframe/{id}")
//	public ResponseEntity<?> deleteWireframe(@PathVariable(value = "id") int id) {
//		boolean deleted = wireframeService.deleteById(id);
//		if (deleted) {
//			SuccessPojo successPojo = new SuccessPojo();
//			Success success = new Success();
//			success.setTitle(Constant.PROJECT_SETUP_API_TITLE);
//			success.setMessage(Constant.PROJECT_DELETED_SUCCESSFULLY);
//			successPojo.setSuccess(success);
//			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
//		} else {
//			ErrorPojo errorPojo = new ErrorPojo();
//			Error error = new Error();
//			error.setTitle(Constant.PROJECT_SETUP_API_TITLE);
//			error.setMessage(Constant.PROJECT_NOT_DELETED);
//			errorPojo.setError(error);
//			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
//		}
//	}

	// ------------- NEED MODIFICATION ---------------
	// COPY WIREFRAME
	@ApiOperation(value = "Copy WireFrame")
	@PostMapping("/wireframe-copy")
	public ResponseEntity<?> copyWireFrame(@Valid @RequestBody WireFrameCopyDTO wireframeCopyDTO) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
//		Long accId = user.getSys_account().getId();

		// MODULE COPY LOGIC
		int from_project_id = wireframeCopyDTO.getFrom_projectId();
		int from_module_id = wireframeCopyDTO.getFrom_moduleId();
		int from_wireframe_id = wireframeCopyDTO.getFrom_WireFrameId();

		String toUiName = wireframeCopyDTO.getTo_uiName();
		// Rn_Module_Setup module = moduleService.getById(from_module_id); // not in use

		Rn_Fb_Header oldHeader = wireframeService.getById(from_wireframe_id);
		// List<Rn_Fb_Line> oldLines = oldHeader.getRn_fb_lines();
		Rn_Fb_Header newHeader = new Rn_Fb_Header();
		newHeader.setCreatedBy(userId);
		newHeader.setUpdatedBy(userId);
//		newHeader.setAccountId(accId);
		newHeader.setTechStack(oldHeader.getTechStack());
		newHeader.setObjectType(oldHeader.getObjectType());
		newHeader.setSubObjectType(oldHeader.getSubObjectType());
		newHeader.setUiName(toUiName); // this is only change
		newHeader.setFormType(oldHeader.getFormType());
		newHeader.setFormCode(toUiName.concat("_view"));
		newHeader.setTableName(oldHeader.getTableName());
		newHeader.setLineTableName(oldHeader.getLineTableName());
		newHeader.setMultilineTableName(oldHeader.getMultilineTableName());
		newHeader.setJspName(oldHeader.getJspName());
		newHeader.setControllerName(oldHeader.getControllerName());
		newHeader.setServiceName(oldHeader.getServiceName());
		newHeader.setServiceImplName(oldHeader.getServiceImplName());
		newHeader.setDaoName(oldHeader.getDaoName());
		newHeader.setDaoImplName(oldHeader.getDaoImplName());
		newHeader.setBuild(oldHeader.isBuild());
		newHeader.setUpdated(oldHeader.isUpdated());
		newHeader.setMenuName(oldHeader.getMenuName());
		newHeader.setHeaderName(oldHeader.getHeaderName());
		newHeader.setConvertedTableName(oldHeader.getControllerName());
		// newHeader.setRn_fb_lines(oldHeader.get);
		newHeader.setModule(oldHeader.getModule());
		// newHeader.setRn_cff_actionBuilder(oldHeader.getRn_cff_actionBuilder());

		Rn_Fb_Header savedHeader = wireframeService.save(newHeader);
		if (savedHeader == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.WIREFRAME_API_TITLE);
			error.setMessage(Constant.WIREFRAME_COPY_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		List<Rn_Fb_Line> oldLines = oldHeader.getRn_fb_lines();
		for (Rn_Fb_Line line : oldLines) {
			Rn_Fb_Line newLine = new Rn_Fb_Line();
			newLine.setCreatedBy(userId);
//			newLine.setAccountId(accId);
			newLine.setForm_type(line.getForm_type());
			newLine.setFieldName(line.getFieldName());
			newLine.setMapping(line.getMapping());
			newLine.setDataType(line.getDataType());
			newLine.setFormCode(toUiName.concat("_view"));
			newLine.setKey1(line.getKey1());
			newLine.setType_field(line.getType_field());
			newLine.setMethodName(line.getMethodName());
			newLine.setSeq(line.getSeq());
			newLine.setSection_num(line.getSection_num());
			newLine.setButton_num(line.getButton_num());
			newLine.setType1(line.getType1());
			newLine.setType2(line.getType2());
			newLine.setMandatory(line.isMandatory());
			newLine.setHidden(line.isHidden());
			newLine.setReadonly(line.isReadonly());
			newLine.setDependent(line.isDependent());
			newLine.setDependent_on(line.getDependent_on());
			newLine.setDependent_sp(line.getDependent_sp());
			newLine.setDependent_sp_param(line.getDependent_sp_param());
			newLine.setValidation_1(line.isValidation_1());
			newLine.setVal_type(line.getVal_type());
			newLine.setVal_sp(line.getVal_sp());
			newLine.setVal_sp_param(line.getVal_sp_param());
			newLine.setSequence(line.isSequence());
			newLine.setSeq_name(line.getSeq_name());
			newLine.setSeq_sp(line.getSeq_sp());
			newLine.setSeq_sp_param(line.getSeq_sp_param());
			newLine.setDefault_1(line.isDefault_1());
			newLine.setDefault_type(line.getDefault_type());
			newLine.setDefault_value(line.getDefault_value());
			newLine.setDefault_sp(line.getDefault_sp());
			newLine.setDefault_sp_param(line.getDefault_sp_param());
			newLine.setCalculated_field(line.isCalculated_field());
			newLine.setCal_sp(line.getCal_sp());
			newLine.setCal_sp_param(line.getCal_sp_param());
			newLine.setAdd_to_grid(line.getAdd_to_grid());
			newLine.setSp_name_for_autocomplete(line.getSp_name_for_autocomplete());
			newLine.setSp_for_autocomplete(line.getSp_for_autocomplete());
			newLine.setSp_name_for_dropdown(line.getSp_name_for_dropdown());
			newLine.setSp_for_dropdown(line.getSp_for_dropdown());
			newLine.setLine_table_name(line.getLine_table_name());
			newLine.setLine_table_no(line.getLine_table_no());
			// newLine.setRn_fb_header(line.getRn_fb_header());
			newLine.setRn_fb_header(savedHeader);
			Rn_Fb_Line savedLine = wireframeService.saveLine(newLine);
			if (savedLine == null) {
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.WIREFRAME_API_TITLE);
				error.setMessage(Constant.WIREFRAME_COPY_FAILURE);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.WIREFRAME_API_TITLE);
		success.setMessage(Constant.WIREFRAME_COPY_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

}
