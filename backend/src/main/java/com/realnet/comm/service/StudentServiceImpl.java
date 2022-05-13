package com.realnet.comm.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.comm.entity.Student;
import com.realnet.comm.repository.StudentRepository;
import com.realnet.comm.repository.TeacherRepository;
import com.realnet.exceptions.ResourceNotFoundException;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudentRepository studentRepository;

//	@Override
//	public List<Student> getAll(int header_id) {
//		return studentRepository.findByHeaderId(header_id);
//	}

	@Override
	public Page<Student> getAll(int header_id, Pageable page) {
		return studentRepository.findByHeaderId(header_id, page);
	}

	@Override
	public Student getByIdAndHeaderId(int id, int header_id) {
		Student student = studentRepository.findByIdAndHeaderId(id, header_id).orElse(null);
		return student;
	}

	@Override
	public Optional<Student> saveByHeaderId(int header_id, Student student) {
		return teacherRepository.findById(header_id).map(teacher -> {
			student.setTeacher(teacher);
			return studentRepository.save(student);
		});
	}

	@Override
	public Student updateByIdAndHeaderId(int id, int header_id, Student student) {
//		if (!studentRepository.existsById(header_id)) {
//			throw new ResourceNotFoundException("teacher id not found :: " + header_id);
//		}
//		return studentRepository.findById(id).map(s -> {
//			s.setName(student.getName());
//			s.setDepertment(student.getDepertment());
//			s.setRollNumber(student.getRollNumber());
//            return studentRepository.save(s);
//        });
		//logger.debug("teacher id = " + header_id + "student id = " + id + ", and Object = " + student.toString());
		Student old_student = studentRepository.findByIdAndHeaderId(id, header_id)
				.orElseThrow(() -> new ResourceNotFoundException("student id not found" + id));
		old_student.setName(student.getName());
		old_student.setDepertment(student.getDepertment());
		old_student.setRollNumber(student.getRollNumber());
		final Student updated_student = studentRepository.save(old_student);
		//logger.debug("updated data = " + updated_student.toString());
		return updated_student;
	}

	@Override
	public boolean deleteByIdAndHeaderId(int id, int header_id) {
		Student student = studentRepository.findByIdAndHeaderId(id, header_id)
				.orElseThrow(() -> new ResourceNotFoundException("student id not found" + id));
		//logger.debug("deleted data = " + student.toString());
		//logger.info(" deleteById() method end");
		studentRepository.delete(student);
		return true;
	}

}
