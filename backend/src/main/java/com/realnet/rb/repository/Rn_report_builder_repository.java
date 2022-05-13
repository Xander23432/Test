package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.Rn_report_builder;

@Repository
public interface Rn_report_builder_repository extends JpaRepository<Rn_report_builder, Integer>{

	
	//@Query(value = ":query", nativeQuery = true)
   // List<Object[]> getQueryData(@Param("sql_query") String query);

	@Query(value = "SELECT * from rn_rb_reports_t WHERE id=:id", nativeQuery = true)
	List<Rn_report_builder> getByReportId(@Param("id") int id);
	
	@Query(value= "SELECT * from rn_rb_reports_t WHERE report_type='service' and id=:id",nativeQuery = true)
	List<Rn_report_builder> getreportbyservice(@Param("id") int id);
}
