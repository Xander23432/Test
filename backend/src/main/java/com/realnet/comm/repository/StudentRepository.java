package com.realnet.comm.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.comm.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
	
	//@Query(value = "SELECT s FROM RN_STUDENT_T s JOIN s.RN_TEACHER_T t WHERE t.teacher_id= :hId")
	@Query(value = "SELECT * FROM rn_student_t WHERE TEACHER_ID = ?1" , nativeQuery = true)
	Page<Student> findByHeaderId(@Param("hId")Integer header_id, Pageable p);
	


	
	//@Query(value = "SELECT s FROM RN_STUDENT_T s JOIN s.RN_TEACHER_T t WHERE s.ID= :id AND t.TEACHER_ID= :hId")
	@Query(value = "SELECT * FROM rn_student_t WHERE ID =:id AND TEACHER_ID =:hId" , nativeQuery = true)
	Optional<Student> findByIdAndHeaderId(@Param("id")Integer id, @Param("hId")Integer header_id);
	
}
