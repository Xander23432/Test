package com.realnet.comm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.comm.entity.Teacher;
import com.realnet.comm.repository.TeacherRepository;
import com.realnet.exceptions.ResourceNotFoundException;

@Service
public class TeacherServiceImpl implements TeacherService {

	//private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

	private final String className = this.getClass().getName();

	@Autowired
	private TeacherRepository teacherRepository;

	@Override
	public List<Teacher> getAll() {
		return teacherRepository.findAll();
	}

	@Override
	public Page<Teacher> getAll(Pageable page) {
		return teacherRepository.findAll(page);
	}

	@Override
	public Teacher getById(int id) {
		//logger.info("getById() method start");
		Teacher teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found :: " + id));
//		if (teacher == null || id != teacher.getId()) {
//			logger.error("teacher not found with id = " + id);
//			logger.debug(teacher.toString());
//		}
//		logger.debug(teacher.toString());
//		logger.info("getById() method end");
		return teacher;
	}

	@Override
	public Teacher save(Teacher teacher) {
		//logger.info(this.className + "save() method start");
		Teacher savedTeacher = teacherRepository.save(teacher);
//		if(savedTeacher.getId() == null) {
//			logger.error("data not saved");
//		}
//		logger.debug("saved data = " + savedTeacher.toString());
//		logger.info(this.className + "save() method end");
		return savedTeacher;
	}

	@Override
	public Teacher updateById(int id, Teacher teacherRequest) {
		Teacher old_teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found :: " + id));
		old_teacher.setName(teacherRequest.getName());
		old_teacher.setEmail(teacherRequest.getEmail());
		old_teacher.setphoneNumber(teacherRequest.getphoneNumber());
		old_teacher.setStudents(teacherRequest.getStudents()); // need to improve
		old_teacher.setExtn1(teacherRequest.getExtn1());
		old_teacher.setExtn2(teacherRequest.getExtn2());
		old_teacher.setExtn3(teacherRequest.getExtn3());
		old_teacher.setExtn4(teacherRequest.getExtn4());
		old_teacher.setExtn5(teacherRequest.getExtn5());
		old_teacher.setExtn6(teacherRequest.getExtn6());
		old_teacher.setExtn7(teacherRequest.getExtn7());
		old_teacher.setExtn8(teacherRequest.getExtn8());
		old_teacher.setExtn9(teacherRequest.getExtn9());
		old_teacher.setExtn10(teacherRequest.getExtn10());
		old_teacher.setExtn11(teacherRequest.getExtn11());
		old_teacher.setExtn12(teacherRequest.getExtn12());
		old_teacher.setExtn13(teacherRequest.getExtn13());
		old_teacher.setExtn14(teacherRequest.getExtn14());
		old_teacher.setExtn15(teacherRequest.getExtn15());
		old_teacher.setFlex1(teacherRequest.getFlex1());
		old_teacher.setFlex2(teacherRequest.getFlex2());
		old_teacher.setFlex3(teacherRequest.getFlex3());
		old_teacher.setFlex4(teacherRequest.getFlex4());
		old_teacher.setFlex5(teacherRequest.getFlex5());
		
		old_teacher.setUpdatedBy(teacherRequest.getUpdatedBy());
		final Teacher updated_teacher = teacherRepository.save(old_teacher);
//		logger.debug("updated data = " + updated_teacher.toString());
//		logger.info(this.className + " updateById() method end");
		return updated_teacher;
	}

	@Override
	public boolean deleteById(int id) {
		if (!teacherRepository.existsById(id)) {
			//logger.error("teacher not found with id = " + id);
			throw new ResourceNotFoundException("Teacher not found :: " + id);
		}
		
//		logger.info(this.className + " deleteById() method start");
//		logger.debug("Input id = " + id);
		Teacher teacher = teacherRepository.findById(id).orElse(null);
//		logger.debug("deleted data = " + teacher.toString());
//		logger.info(this.className + " deleteById() method end");
		teacherRepository.delete(teacher);
		return true;
	}

}
