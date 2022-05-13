package com.realnet.department.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.realnet.department.entity.Departmententity;
@Repository
public interface Departmentrepository extends JpaRepository<Departmententity, Integer> {
	Page<Departmententity> findAll(Pageable p);
	
}
