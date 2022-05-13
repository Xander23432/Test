package com.realnet.bi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.bi.entity.Rn_Dashboard_Widgets;
import com.realnet.comm.entity.Order;

@Repository
public interface Rn_dashboard_widget_repository extends JpaRepository<Rn_Dashboard_Widgets, Integer>{

	@Query(value = "select * from rn_dashboard_widgets_t where module_id =:id", nativeQuery = true)
	List<Rn_Dashboard_Widgets> getByModule(@Param("id") int id);
}
