package com.realnet.fnd.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.realnet.comm.entity.CollegeStudentEntity;
@Repository
public interface CollegeStudentRepository extends JpaRepository<CollegeStudentEntity, Integer> {
	Page<CollegeStudentEntity> findAll(Pageable p);
	
}