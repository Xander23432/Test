package com.realnet.fnd.repository1;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.fnd.entity1.MenuDet;

@Repository
public interface MenuDetRepository extends JpaRepository<MenuDet,Long>{
	@Query(value="select a.menu_item_id,a.menu_item_desc, \n"
			+ "    a.menu_id,b.m_create,b.m_visible,\n"
			+ "    b.m_edit,b.m_query,b.m_delete\n"
			+ "from sec_menu_det a,sec_grp_menu_access b\n"
			+ "where b.menu_item_id=a.menu_item_id\n"
			+ "order by a.item_seq",
			countQuery="select count(*) "
					+ "from sec_menu_det a,sec_grp_menu_access b\n"
					+ "where b.menu_item_id=a.menu_item_id\n"
					+ "order by a.item_seq",nativeQuery=true)
		Page<Object> getAll(Pageable page);
	
	@Query(value="select a.menu_item_id,a.menu_item_desc, \n"
			+ "    a.menu_id,b.m_create,b.m_visible,\n"
			+ "    b.m_edit,b.m_query,b.m_delete\n"
			+ "from sec_menu_det a,sec_grp_menu_access b\n"
			+ "where b.menu_item_id=a.menu_item_id\n"
			+ "and b.usr_grp = ?1 "
			+ "and a.menu_id = ?2 "
			+ "order by a.item_seq",countQuery="select count(*) "
					+ "from sec_menu_det a,sec_grp_menu_access b\n"
					+ "where b.menu_item_id=a.menu_item_id\n"
					+ "and b.usr_grp = ?1 "
					+ "and a.menu_id = ?2 "
					+ "order by a.item_seq;"
			,nativeQuery = true)
	List<Object> getById(Long usr_grp,Long menu_id);
	
	@Query(value="select a.menu_item_id,a.menu_item_desc,\n"
			+ "    a.menu_id,b.m_create,b.m_visible,\n"
			+ "    b.m_edit,b.m_query,b.m_delete,\n"
			+ " a.main_menu_action_name,a.main_menu_icon_name "
			+ "from sec_menu_det a,sec_grp_menu_access b\n"
			+ "where b.menu_item_id=a.menu_item_id\n"
			+ "and b.usr_grp = ?1\n"
			+ "and a.menu_id= ?2 "
			+ "order by a.item_seq",
			countQuery="select count(*) "
					+ "from sec_menu_det a,sec_grp_menu_access b\n"
					+ "where b.menu_item_id=a.menu_item_id\n"
					+ "and b.usr_grp = ?1"
					+ "and a.menu_id= ?2 "
					+ "order by a.item_seq",
					nativeQuery=true)
	List<Object> getByUserId(Long usr_grp,Long menu_id);
	
}
