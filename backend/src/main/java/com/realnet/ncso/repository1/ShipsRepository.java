package com.realnet.ncso.repository1;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.Ships;

@Repository
public interface ShipsRepository extends JpaRepository<Ships,String>{
	@Query(value = "SELECT vr.vessel_reg_id vessel_code,\n"
			+ "			vr.vessel_name,\n"
			+ "			vr.vcn,\n"
			+ "			vr.call_sign,\n"
			+ "			vr.line_code,\n"
			+ "			ceil(vr.loa) loa,\n"
			+ "			vr.grt gt,\n"
			+ "			ad.ata,\n"
			+ "			ad.atd\n"
			+ "			From pmod_voyage_reg vr,pmod_arrival_depart ad\n"
			+ "			WHERE\n"
			+ "			 ad.atd is not null\n"
			+ "			 AND vr.vcn = ad.vcn\n"
			+ "			 AND ( vr.vessel_reg_id LIKE NVL( :str ,vr.vessel_reg_id)\n"
			+ "			 OR vr.vessel_name LIKE NVL( :str ,vr.vessel_name)\n"
			+ "			 OR vr.vcn LIKE NVL( :str ,vr.vcn)\n"
			+ "			 OR vr.call_sign LIKE NVL( :str ,vr.call_sign)\n"
			+ "			 OR vr.line_code LIKE NVL( :str ,vr.line_code)\n"
			+ "			 OR vr.grt LIKE NVL( :str,vr.grt)\n"
			+ "			 OR ad.ata LIKE NVL( :str,ad.ata)\n"
			+ "			 OR ad.atd LIKE NVL( :str,ad.atd) ) "
			+ "			 ORDER BY ad.atd DESC",
			countQuery="   select\n"
					+ "        count(*) \n"
					+ "    From\n"
					+ "        pmod_voyage_reg vr,\n"
					+ "        pmod_arrival_depart ad    \n"
					+ "    WHERE\n"
					+ "        ad.atd is not null     \n"
					+ "        AND vr.vcn = ad.vcn     \n"
					+ "        AND (\n"
					+ "            vr.vessel_reg_id LIKE NVL( :str,vr.vessel_reg_id)     \n"
					+ "            OR vr.vessel_name LIKE NVL( :str,vr.vessel_name)     \n"
					+ "            OR vr.vcn LIKE NVL( :str,vr.vcn)     \n"
					+ "            OR vr.call_sign LIKE NVL( :str,vr.call_sign)     \n"
					+ "            OR vr.line_code LIKE NVL( :str,vr.line_code)     \n"
					+ "            OR vr.grt LIKE NVL( :str,vr.grt)     \n"
					+ "            OR ad.ata LIKE NVL( :str,ad.ata)     \n"
					+ "            OR ad.atd LIKE NVL( :str,ad.atd)      \n"
					+ "        )",nativeQuery = true)
	List<Object> getAllPmod(@Param("str") String str);
	
	
	
	
	
	
	
	
	
	
	@Query(value="select\n"
			+ "s.id vessel_code, "
			+ "s.name vessel_name, "
			+ "sv.in_voy_nbr in_voyage, "
			+ "sv.out_voy_nbr out_voyage, "
			+ "sv.out_call_nbr call_no, "
			+ "s.line_id line_code, "
			+ "sc.loa, "
			+ "sc.loa_units loa_uom, "
			+ "sc.grt gt, "
			+ "sv.ata, "
			+ "sv.atd, "
			+ "sv.berth location, "
			+ "sy.service_id "
			+ "from ship_voyages sy ,ship_visits sv ,ships s,ship_classes sc "
			+ "where sy.ship_id=sv.ship_id "
			+ "and s.id=sv.ship_id "
			+ "and s.sclass_id=sc.id "
			+ "and sy.nbr = sv.out_voy_nbr "
			+ "and sv.atd is not null "
			+ " order by sv.atd desc",
			countQuery = "select "
					+ "        count(*) "
					+ "    from "
					+ "        ship_voyages sy, "
					+ "        ship_visits sv, "
					+ "        ships s, "
					+ "        ship_classes sc  "
					+ "    where "
					+ "        sy.ship_id=sv.ship_id  "
					+ "        and s.id=sv.ship_id  "
					+ "        and s.sclass_id=sc.id  "
					+ "        and sy.nbr = sv.out_voy_nbr  "
					+ "        and sv.atd is not null"
			,nativeQuery=true)
	Page<Object> getAll(Pageable page); 
	
	@Query(value="select\n"
			+ "s.id vessel_code,\n"
			+ "s.name vessel_name,\n"
			+ "sv.in_voy_nbr in_voyage,\n"
			+ "sv.out_voy_nbr out_voyage,\n"
			+ "sv.out_call_nbr call_no,\n"
			+ "s.line_id line_code,\n"
			+ "sc.loa,\n"
			+ "sc.loa_units loa_uom,\n"
			+ "sc.grt gt,\n"
			+ "sv.ata,\n"
			+ "sv.atd,\n"
			+ "sv.berth location,\n"
			+ "sy.service_id\n"
			+ "from ship_voyages sy ,ship_visits sv ,ships s,ship_classes sc\n"
			+ "where sy.ship_id=sv.ship_id\n"
			+ "and s.id=sv.ship_id\n"
			+ "and s.sclass_id=sc.id\n"
			+ "and sy.nbr = sv.out_voy_nbr\n"
			+ "and sv.atd is not null\n"
			+ "and s.name = ?1  "
			+ "and sv.in_voy_nbr = ?2 "
			+ " order by sv.atd desc ",nativeQuery=true)
	Optional<Object> getOneNovis(String name,String inVoyNbr);
	
	@Query(value="SELECT vr.vessel_reg_id vessel_code,\n"
			+ "vr.vessel_name,\n"
			+ "vr.vcn,\n"
			+ "vr.call_sign,\n"
			+ "vr.line_code,\n"
			+ "ceil(vr.loa) loa,\n"
			+ "vr.grt gt,\n"
			+ "ad.ata,\n"
			+ "ad.atd \n"
			+ "From pmod_voyage_reg vr,pmod_arrival_depart ad \n"
			+ "WHERE\n"
			+ "ad.atd is not null \n"
			+ "AND vr.vcn = ad.vcn \n"
			+ "and vr.vcn  = ?1 "
			+ "ORDER BY ad.atd DESC",nativeQuery = true)
	Optional<Object> getOnePmod(String vcn);

	
	



	@Query(value="select\n"
			+ "			s.id vessel_code,\n"
			+ "			s.name vessel_name,\n"
			+ "			sv.in_voy_nbr in_voyage,\n"
			+ "			sv.out_voy_nbr out_voyage,\n"
			+ "			sv.out_call_nbr call_no,\n"
			+ "			s.line_id line_code,\n"
			+ "			sc.loa,\n"
			+ "			sc.loa_units loa_uom,\n"
			+ "			sc.grt gt,\n"
			+ "			sv.ata,\n"
			+ "			sv.atd,\n"
			+ "			sv.berth location,\n"
			+ "			sy.service_id\n"
			+ "			from ship_voyages sy ,ship_visits sv ,ships s,ship_classes sc\n"
			+ "			where sy.ship_id=sv.ship_id\n"
			+ "			and s.id=sv.ship_id\n"
			+ "			and s.sclass_id=sc.id\n"
			+ "			and sy.nbr = sv.out_voy_nbr\n"
			+ "			and sv.atd is not null\n"
			+ "			and (s.id LIKE NVL(:str,s.id)\n"
			+ "			OR s.name LIKE NVL(:str,s.name) \n"
			+ "			OR sv.in_voy_nbr LIKE NVL(:str,sv.in_voy_nbr)\n"
			+ "			OR sv.out_voy_nbr LIKE NVL(:str,sv.out_voy_nbr)\n"
			+ "			OR sv.out_call_nbr LIKE NVL(:str,sv.out_call_nbr)\n"
			+ "			OR s.line_id LIKE NVL(:str,s.line_id)\n"
			+ "			OR sc.loa LIKE NVL(:str,sc.loa)\n"
			+ "			OR sc.loa_units LIKE NVL(:str,sc.loa_units)\n"
			+ "			OR sc.grt LIKE NVL(:str,sc.grt)\n"
			+ "			OR sv.ata LIKE NVL(:str,sv.ata)\n"
			+ "			OR sv.atd LIKE NVL(:str,sv.atd)\n"
			+ "			OR sv.berth LIKE NVL(:str,sv.berth)\n"
			+ "			OR sy.service_id LIKE NVL(:str,sy.service_id))\n"
			+ "			order by sv.atd DESC",
			countQuery="select\n"
					+ " count(*) "
					+ "			from ship_voyages sy ,ship_visits sv ,ships s,ship_classes sc\n"
					+ "			where sy.ship_id=sv.ship_id\n"
					+ "			and s.id=sv.ship_id\n"
					+ "			and s.sclass_id=sc.id\n"
					+ "			and sy.nbr = sv.out_voy_nbr\n"
					+ "			and sv.atd is not null\n"
					+ "			and (s.id LIKE NVL(:str,s.id)\n"
					+ "			OR s.name LIKE NVL(:str,s.name) \n"
					+ "			OR sv.in_voy_nbr LIKE NVL(:str,sv.in_voy_nbr)\n"
					+ "			OR sv.out_voy_nbr LIKE NVL(:str,sv.out_voy_nbr)\n"
					+ "			OR sv.out_call_nbr LIKE NVL(:str,sv.out_call_nbr)\n"
					+ "			OR s.line_id LIKE NVL(:str,s.line_id)\n"
					+ "			OR sc.loa LIKE NVL(:str,sc.loa)\n"
					+ "			OR sc.loa_units LIKE NVL(:str,sc.loa_units)\n"
					+ "			OR sc.grt LIKE NVL(:str,sc.grt)\n"
					+ "			OR sv.ata LIKE NVL(:str,sv.ata)\n"
					+ "			OR sv.atd LIKE NVL(:str,sv.atd)\n"
					+ "			OR sv.berth LIKE NVL(:str,sv.berth)\n"
					+ "			OR sy.service_id LIKE NVL(:str,sy.service_id))\n"
					+ "			order by sv.atd DESC",
			nativeQuery=true)
	List<Object> getAllSearch(@Param("str") String str);
}
