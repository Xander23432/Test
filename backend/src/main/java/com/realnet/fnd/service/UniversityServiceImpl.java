package com.realnet.fnd.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import com.realnet.comm.entity.UniversityEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.repository.UniversityRepository;

@Service
public class UniversityServiceImpl implements UniversityService {

	@Autowired
	private UniversityRepository authrepo;

	@Override
	public Page<UniversityEntity> getAll(Pageable page) {
		// TODO Auto-generated method stub
		return authrepo.findAll(page);
		
	}

	@Override
	public UniversityEntity getById(int id) {
		// TODO Auto-generated method stub
		UniversityEntity teacher = authrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found :: " + id));

		return teacher;
	}

	@Override
	public UniversityEntity save(UniversityEntity teacher) {
		// TODO Auto-generated method stub
		UniversityEntity savedTeacher = authrepo.save(teacher);
		return savedTeacher;
	}

	@Override
	public UniversityEntity updateById(int id, UniversityEntity teacherRequest) {
		// TODO Auto-generated method stub
		UniversityEntity old_teacher = authrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found :: " + id));
old_teacher.setName(teacherRequest.getName());
old_teacher.setEmail(teacherRequest.getEmail());
old_teacher.setSubject(teacherRequest.getSubject());
old_teacher.setPhone(teacherRequest.getPhone());
old_teacher.setBook(teacherRequest.getBook());
		
		
		final UniversityEntity updated_teacher = authrepo.save(old_teacher);

		return updated_teacher;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if (!authrepo.existsById(id)) {
			throw new ResourceNotFoundException("author not found :: " + id);
		}
		
		UniversityEntity teacher = authrepo.findById(id).orElse(null);
		authrepo.delete(teacher);
		return true;
	}

}
