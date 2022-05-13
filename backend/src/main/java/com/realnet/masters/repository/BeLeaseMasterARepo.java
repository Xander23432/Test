package com.realnet.masters.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.masters.entity.BeLeaseMasterA;

@Repository
public interface BeLeaseMasterARepo extends JpaRepository<BeLeaseMasterA,Long>{

	List<BeLeaseMasterA> findBychangedDateBetween(Date date1, Date date2);
	@Query(value="select * from BE_LEASE_MASTER_A where changed_by=?1 and changed_date between ?2 and ?3",
			countQuery="select COUNT(*) from BE_ITEM_MASTER_A where changed_by=?1 and changed_date between ?2 and ?3",
			nativeQuery=true)
	List<BeLeaseMasterA> findCustom(Long id, Date date1, Date date2);

}
