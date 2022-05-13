package com.realnet.department.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.realnet.department.entity.Departmententity;
public interface Departmentservice {
	 public Page<Departmententity> getlist(Pageable page);
	    public Departmententity createCollageStudent(Departmententity data);
	    public Departmententity getid(int id);
	    public Departmententity updateById(int id, Departmententity departmententity);
	    public boolean deleteById(int id);
}
