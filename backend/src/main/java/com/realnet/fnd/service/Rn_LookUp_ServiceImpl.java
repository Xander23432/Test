package com.realnet.fnd.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.fnd.repository.Rn_LookUpRepository;
import com.realnet.qb.entity.Rn_CreateQuery;
import com.realnet.qb.service.Rn_CreateQuery_Service;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;
import com.realnet.wfb.entity.Rn_Fb_Header;
import com.realnet.wfb.entity.Rn_Fb_Line;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_LookUp_ServiceImpl implements Rn_LookUp_Service {

	@Autowired
	private Rn_LookUpRepository lookUpRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Rn_CreateQuery_Service createQueryService;
	
	@Autowired(required=false)
	private UserService userService;
	
	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Override
	public List<Rn_Lookup_Values> getAll() {
		return lookUpRepository.findAll();
	}

	@Override
	public Page<Rn_Lookup_Values> getAll(Pageable page) {
		return lookUpRepository.findAll(page);
	}

	@Override
	public Rn_Lookup_Values getById(int id) {
		Rn_Lookup_Values bcf_extractor = lookUpRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return bcf_extractor;
	}

	@Override
	public Rn_Lookup_Values save(Rn_Lookup_Values bcf_extractor) {
		Rn_Lookup_Values savedTechnology = lookUpRepository.save(bcf_extractor);
		return savedTechnology;
	}

	@Override
	public Rn_Lookup_Values updateById(int id, Rn_Lookup_Values lookUpRequest) {
		Rn_Lookup_Values oldLookUp = lookUpRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		oldLookUp.setLookupCode(lookUpRequest.getLookupCode());
		oldLookUp.setMeaning(lookUpRequest.getMeaning());
		oldLookUp.setDescription(lookUpRequest.getDescription());
		oldLookUp.setLookupType(lookUpRequest.getLookupType());
		oldLookUp.setActive_start_date(lookUpRequest.getActive_start_date());
		oldLookUp.setActive_end_date(lookUpRequest.getActive_end_date());
		oldLookUp.setEnabled_flag(lookUpRequest.isEnabled_flag());
		// WHO
		oldLookUp.setUpdatedBy(lookUpRequest.getUpdatedBy());
		final Rn_Lookup_Values updatedLookUp = lookUpRepository.save(oldLookUp);
		return updatedLookUp;
	}

	@Override
	public boolean deleteById(int id) {
		if (!lookUpRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Lookup_Values bcf_extractor = lookUpRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		lookUpRepository.delete(bcf_extractor);
		return true;
	}

	@Override
	public List<String> getLookupValues() {
		return lookUpRepository.findLookupValues();
	}

	@Override
	public List<String> getDataTypeValues() {
		return lookUpRepository.findDataTypeValues();
	}

	@Override
	public List<Rn_Lookup_Values> getExtensions() {
		return lookUpRepository.findExtensions();
	}

	@Override
	public void createTable(int id) throws IOException {
		System.out.println("Calling Service");
StringBuilder sqlQuery = new StringBuilder();
		
		// get data from Angular Form (form data)
		/*
		 * boolean data = exportDataDTO.getData(); System.out.println("id::"+id);
		 * System.out.println("valus of data"+data); if (data) {
		 * System.out.println("Data Present"); } else {
		 * System.out.println("Data Not Present"); }
		 */
				
		
		// Rn_Fb_Header table value by Id
		Rn_Fb_Header fbh = wireFrameService.getById(id);
		String table_name = fbh.getTableName();
		String line_table_name = fbh.getLineTableName();
		String form_type = fbh.getFormType();
		
		System.out.println("Table Name::"+table_name+"\n\nForm Type:;"+form_type);

		// Rn_Fb_Line table value by Id
		List<Rn_Fb_Line> fbl = fbh.getRn_fb_lines();
		String mapping = fbl.get(0).getMapping();

		if (form_type.equals("header_only") || form_type.equals("header_line") || form_type.equals("multiline")) {
			String dropQuery = "DROP TABLE IF EXISTS " + table_name + "";
			jdbcTemplate.execute(dropQuery);
		} else if (form_type.equals("line_only") || form_type.equals("header_line") || form_type.equals("multiline")) {
			String dropQuery = "DROP TABLE IF EXISTS " + line_table_name + "";
			jdbcTemplate.execute(dropQuery);
		}

		if (form_type.equals("line_only")) {
			sqlQuery.append("create table if not exists " + line_table_name + " (");
		} else if (form_type.equals("header_only")) {
			sqlQuery.append("create table if not exists " + table_name + " (");
		}

		for (int i = 0; i < fbl.size(); i++) 
		{
			System.out.println("Line for loop::"+fbl.get(i).getKey1());
			mapping = fbl.get(i).getMapping();
			String data_type = fbl.get(i).getDataType();
			String key1 = fbl.get(i).getKey1();
			  if(key1 != null && !key1.isEmpty()) 
			  {
				System.out.println("Line for loop under check null if::"+fbl.get(i).getKey1());
				if (key1.equals("PRI")) {
					sqlQuery.append(mapping + " " + data_type + "(45) NOT NULL AUTO_INCREMENT, ");
				} else if (data_type.equals("longtext") || data_type.equals("datetime") || data_type.equals("double")) {
					sqlQuery.append(mapping + " " + data_type + ", ");
				} else {
					sqlQuery.append(mapping + " " + data_type + "(45), ");
				}
			  }
		}
		
		StringBuilder attributeFlexData = new StringBuilder(); 
		List<Rn_Lookup_Values> attribute_flex =getExtensions();
		for (int i = 0; i < attribute_flex.size(); i++) {
			String lookup_code = attribute_flex.get(i).getLookupCode();
			String lower_case = lookup_code.toLowerCase();
//			String first_upper = RealNetUtils.toFirstUpperCase(lower_case);
//			String only_upper = lookup_code.toUpperCase();
			attributeFlexData.append(lower_case + " varchar(25), ");
			
		}
		System.out.println("Attribute flex Query = " + attributeFlexData.toString());
		//sqlQuery.append(attributeFlexData +" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime, PRIMARY KEY(id));");
		
		sqlQuery.append(
				" account_id int(11), CREATED_BY varchar(25),LAST_UPDATED_BY varchar(25), CREATION_DATE datetime, LAST_UPDATE_DATE datetime,"
						+ " extn1 varchar(25),extn2 varchar(25),extn3 varchar(25),extn4 varchar(25),extn5 varchar(25),extn6 varchar(25),"
						+ " extn7 varchar(25),extn8 varchar(25),extn9 varchar(25),extn10 varchar(25),extn11 varchar(25),extn12 varchar(25),"
						+ " extn13 varchar(25),extn14 varchar(25),extn15 varchar(25), flex1 varchar(25), flex2 varchar(25), flex3 varchar(25), flex4 varchar(25),"
						+ " flex5 varchar(25),  PRIMARY KEY(id));");

		System.out.println("Spring Boot sqlQuery = " + sqlQuery);

		String final_query = sqlQuery.toString().toLowerCase();
		jdbcTemplate.execute(final_query);

		User loggedInUser = userService.getLoggedInUser();
		long user_id = loggedInUser.getUserId();
		long account_id = loggedInUser.getSys_account().getId();
		int project_id = fbh.getModule().getProject().getId();
		
		// save data into rn_create_query_t Table
		Rn_CreateQuery rn_create_query_t = new Rn_CreateQuery();
		rn_create_query_t.setProjectId(project_id);
		rn_create_query_t.setAccountId(account_id);
		rn_create_query_t.setTableName(table_name);
		rn_create_query_t.setCreateQuery(final_query);
		rn_create_query_t.setCreatedBy(user_id);
		rn_create_query_t.setUpdatedBy(user_id);
		rn_create_query_t.setBuild(false);
		//rn_create_query_t.setData(data);

		Rn_CreateQuery savedQuery = createQueryService.save(rn_create_query_t);
		
		System.out.println("Save query in create table"+savedQuery);
		
		
	}
	

}
