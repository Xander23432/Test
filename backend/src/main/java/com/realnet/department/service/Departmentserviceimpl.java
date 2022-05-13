package com.realnet.department.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.realnet.department.entity.Departmententity;
import com.realnet.department.repository.Departmentrepository;
import com.realnet.department.service.Departmentservice;
import com.realnet.exceptions.ResourceNotFoundException;
@Service
public class Departmentserviceimpl implements Departmentservice {

	@Autowired
	private	Departmentrepository  departmentrepository;
	public Page<Departmententity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return departmentrepository.findAll(page);
	}
	
	

	@Override
	public Departmententity createCollageStudent(Departmententity data) {
		// TODO Auto-generated method stub
		return departmentrepository.save(data);
	}

	@Override
	public Departmententity getid(int id) {
		// TODO Auto-generated method stub
		Departmententity orElseThrow = this.departmentrepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rn_Instructor not found :: " + id));
		return orElseThrow;
	}

	@Override
	public Departmententity updateById(int id, Departmententity departmentEntity) {
		// TODO Auto-generated method stub
		Departmententity department = departmentrepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(" not found :: " + id));
			
		department.setDepartment_code(departmentEntity.getDepartment_code());
		
		
		department.setDescription(departmentEntity.getDescription());
		
		
		department.setActive(departmentEntity.getActive());
		
		
		department.setCreated_by(departmentEntity.getCreated_by());
		
		
		department.setCreated_on(departmentEntity.getCreated_on());

        department.setUpdated_by(departmentEntity.getUpdated_by());
		
		
		department.setUpdated_on(departmentEntity.getUpdated_on());
		
		departmentrepository.save(department);
		
		return department;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
if (!departmentrepository.existsById(id)) {
			
			throw new ResourceNotFoundException(" not found :: " + id);
		}
		
          Departmententity department = departmentrepository.findById(id).orElse(null);
          departmentrepository.delete(department);
		return true;
	}

}
