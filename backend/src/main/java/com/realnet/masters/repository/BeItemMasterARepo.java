package com.realnet.masters.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.masters.entity.BeItemMasterA;

@Repository
public interface BeItemMasterARepo extends JpaRepository<BeItemMasterA,Long>{
	public List<BeItemMasterA> findBychangedDateBetween(Date start,Date end);
//	public List<BeItemMasterA> findBychangedByAndchangedDateBetween(Long changedBy,Date start,Date end);
	@Query(value="select * from BE_ITEM_MASTER_A where changed_by=?1 and changed_date between ?2 and ?3",
			countQuery="select COUNT(*) from BE_ITEM_MASTER_A where changed_by=?1 and changed_date between ?2 and ?3",
			nativeQuery=true)
	public List<BeItemMasterA> findCustom(Long changedBy,Date start,Date end);
	@Query(value="SELECT *\n"
			+ "from BE_ITEM_MASTER_A\n"
			+ "WHERE changed_by=NVL(?1, changed_by)\n"
			+ "and  CHANGED_DATE between NVL(?2, CHANGED_DATE) and NVL(?3, CHANGED_DATE)",
			nativeQuery=true)
	public List<BeItemMasterA> findCustomNullcheck(Long changedBy,Date start,Date end);
}
