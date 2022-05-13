package com.realnet.fnd.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.flf.entity.Rn_Bcf_Technology_Stack;
import com.realnet.fnd.entity.DropDownDTO;
import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.repository.Rn_ProjectSetup_Repository;
import com.realnet.users.entity.User;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service.UserService;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Rn_ProjectSetup_ServiceImpl implements Rn_ProjectSetup_Service {

	@Value("${projectPath}")
	private String projectPath;

	@Autowired
	private Rn_ProjectSetup_Repository projectSetupRepository;

	@Autowired
	private com.realnet.flf.service.Rn_Bcf_TechnologyStack_Service technologyStackService;
	
	@Autowired
	private AppUserServiceImpl userService;

	@Override
	public List<Rn_Project_Setup> getAll() {
		return projectSetupRepository.findAll();
	}

	@Override
	public Page<Rn_Project_Setup> getAll(Pageable page) {
		return projectSetupRepository.findAll(page);
	}

	@Override
	public Rn_Project_Setup getById(int id) {
		Rn_Project_Setup bcf_extractor = projectSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return bcf_extractor;
	}

	@Override
	public Rn_Project_Setup save(Rn_Project_Setup project) {
		AppUser loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
//		long accId = loggedInUser.getSys_account().getId();
		project.setCreatedBy(userId);
		project.setUpdatedBy(userId);
		Rn_Bcf_Technology_Stack technologyStack = technologyStackService.getById(project.getTechStackId());
		project.setTechnologyStack(technologyStack.getTech_stack());
//		project.setAccountId(accId);
		Rn_Project_Setup savedProject = projectSetupRepository.save(project);
		return savedProject;
	}

	@Override
	public Rn_Project_Setup updateById(int id, Rn_Project_Setup projectRequest) {
		AppUser loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
		
		Rn_Project_Setup oldProject = projectSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		oldProject.setProjectName(projectRequest.getProjectName());
		oldProject.setDescription(projectRequest.getDescription());
		
//		oldProject.setModules(projectRequest.getModules());
		
		oldProject.setCopyTo(projectRequest.getCopyTo());
		oldProject.setTechnologyStack(projectRequest.getTechnologyStack());
		oldProject.setTechStackId(projectRequest.getTechStackId());
		oldProject.setProjectPrefix(projectRequest.getProjectPrefix());
		oldProject.setDbName(projectRequest.getDbName());
		oldProject.setDbUserName(projectRequest.getDbUserName());
		oldProject.setDbPassword(projectRequest.getDbPassword());
		oldProject.setPortNumber(projectRequest.getPortNumber());
		oldProject.setUpdatedBy(userId);
		final Rn_Project_Setup updatedProject = projectSetupRepository.save(oldProject);
		return updatedProject;
	}

	@Override
	public boolean deleteById(int id) {
		if (!projectSetupRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_Project_Setup bcf_extractor = projectSetupRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		projectSetupRepository.delete(bcf_extractor);
		return true;
	}

	@Override
	public boolean moveUploadedTechnologyToBaseProjectDir(Rn_Project_Setup project) {
		String projectName = project.getProjectName();
		String dbName = project.getDbName();
		String dbPassword = project.getDbPassword();
		String dbUserName = project.getDbUserName();
		String portNumber = project.getPortNumber();
		int techId = project.getTechStackId();
		Rn_Bcf_Technology_Stack technologyStack = technologyStackService.getById(techId);
		project.setTechnologyStack(technologyStack.getTech_stack());// String project_name = technologyStack.get
		String base_prj_folder_name = technologyStack.getBase_prj_file_name();
		System.out.println("Base project folder name = " + base_prj_folder_name);

		String techStack = technologyStack.getTech_stack();
		String dynamicPath = projectPath + "/BaseProject/" + techStack + "/" + base_prj_folder_name;
		System.out.println("Dynamic Path by ganesh bute= " + dynamicPath);
//		try {
//			// add base source code
//			File source = new File(dynamicPath);
//			File dest = new File(projectPath + "/Projects/" + projectName); // need to modifyyy the project name
//
//			System.out.println("SOURCE = " + source + "\nDEST = " + dest);
//			FileUtils.copyDirectory(source, dest);
//
//			StringBuilder properties = new StringBuilder();
//
//			properties.append("server.contextPath=/\r\n"
//					+ "server.port=9119\r\n"
//					+ "spring.resources.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/,classpath:/webui/\r\n"
//					+ "\r\n"
//					+ "spring.banner.location=classpath:banner_txt.txt\r\n"
//					+ "\r\n"
//					+ "\r\n"
//					+ "springfox.documentation.swagger.v2.path=/api-docs\r\n"
//					+ "spring.jackson.date-format=yyyy-MM-dd\r\n"
//					+ "\r\n"
//					+ "spring.datasource.url=jdbc:mysql://localhost:"+portNumber+"/"+dbName+"\r\n"
//					+ "spring.datasource.username="+dbUserName+"\r\n"
//					+ "spring.datasource.password="+dbPassword+"\r\n"
//					+ "spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver\r\n"
//					+ "#spring.jpa.show-sql=true\r\n"
//					+ "spring.jpa.hibernate.ddl-auto=update\r\n"
//					+ "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect\r\n"
//					+ "\r\n"
//					+ "\n projectPath=" + projectPath + "/Projects/" + projectName
//					+ "\r\n"
//					+"\n angularProjectPath="+ projectPath + "/Projects/" + projectName+"/webui"
//					+ "\r\n"
//					+ "#***** MAIL SENDER\r\n"
//					+ "spring.mail.host=smtp.gmail.com\r\n"
//					+ "spring.mail.username=ganesh.bute@dekatc.com\r\n"
//					+ "spring.mail.password=real@123");
//			
////			properties.append("\njdbc.driverClassName = com.mysql.jdbc.Driver" + "\njdbc.url = jdbc:mysql://@localhost:"
////					+ portNumber + "/" + dbName + "\njdbc.username = " + dbUserName + "\njdbc.password = " + dbPassword
////					+ "\nhibernate.dialect = org.hibernate.dialect.MySQLDialect" + "\nhibernate.show_sql = true"
////					+ "\nhibernate.format_sql = true6" + "\nhibernate.use_sql_comments = true"
////					+ "\nhibernate.hbm2ddl.auto=update" + "\nhibernate.enable_lazy_load_no_trans=true"
////					+ "\nprojectPath=" + projectPath + "/Projects/" + projectName); // Dest
//
//			File file0 = new File(
//					projectPath + "/Projects/" + projectName + "/src/main/resources/application.properties");
//			File parentDir = new File(file0.getParent());
//			parentDir.mkdirs();
//			if (!file0.exists()) {
//				file0.createNewFile();
//			}
//			FileWriter fw = new FileWriter(file0.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(properties.toString());
//			bw.close();
//		} catch (FileNotFoundException e) {
//			log.error("File not found exception Handled", e.getMessage());
//			return false;
//		} catch (IOException e) {
//			log.error("IO Exception exception Handled", e.getMessage());
//			return false;
//		}
		log.info("successfully executed...");
		return true;
	}
	
	@Override
	public List<DropDownDTO> getprojectsForDropDown() {
		//List<Rn_Project_Setup> projectList = projectSetupRepository.findProjectsForDropDown();
		List<Rn_Project_Setup> projectList = this.getAll();
		
		ArrayList<DropDownDTO> dtos = new ArrayList<DropDownDTO>();
		for(Rn_Project_Setup project: projectList) {
			DropDownDTO dto = new DropDownDTO();
			dto.setId(project.getId());
			dto.setName(project.getProjectName());
			dtos.add(dto);
		}
		return dtos;
	}

//	@Override
//	public List<Rn_Project_Setup> copyProject(String from_tech_stack, String from_object_type,
//			String from_sub_object_type) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
