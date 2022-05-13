package com.realnet.fnd.controller1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.fnd.entity1.GrpMenuAccess;
import com.realnet.fnd.entity1.MixMenu;
import com.realnet.fnd.service1.GrpMenuAccessServiceImpl;
import com.realnet.fnd.service1.MenuDetServiceImpl;
import com.realnet.users.entity1.AppUserRole;
import com.realnet.users.service1.AppUserServiceImpl;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api1", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_Menu_Group" })
@CrossOrigin("*")
public class MenuController {
	private MenuDetServiceImpl menuDetServiceImpl;
	private GrpMenuAccessServiceImpl grpMenuAccessServiceImpl;
	private AppUserServiceImpl appUserServiceImpl;
	@Autowired
	public MenuController(MenuDetServiceImpl menuDetServiceImpl,
			GrpMenuAccessServiceImpl grpMenuAccessServiceImpl,
			AppUserServiceImpl appUserServiceImpl) {
		super();
		this.menuDetServiceImpl = menuDetServiceImpl;
		this.grpMenuAccessServiceImpl = grpMenuAccessServiceImpl;
		this.appUserServiceImpl = appUserServiceImpl;
	}
	@GetMapping("/allMenuDet")
	public ResponseEntity<?> getAllMenuDet(){
		//List<MenuDet> l = menuDetServiceImpl.getAll();
		List<Object> l =menuDetServiceImpl.getAllObject( PageRequest.of(0,5));
		List<MixMenu> m = new ArrayList<MixMenu>();
		for(Object o:l) {
			Object[] e = (Object[]) o;	
			MixMenu p = new MixMenu();
			p.setMenuItemId( (BigDecimal) e[0]);
			p.setMenuItemDesc((String) e[1]);
			p.setMenuId((BigDecimal) e[2]);
			p.setMCreate((String) e[3]);
			p.setMVisible((String) e[4]);
			p.setMEdit((String) e[5]);
			p.setMQuery((String) e[6]);
			p.setMDelete((String) e[7]);
			m.add(p);
		}
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
	@GetMapping("/getByMenuId/{usrGrp}/{menuId}")
	public ResponseEntity<?> getOne(@PathVariable("usrGrp") Long usrGrp,@PathVariable("menuId") Long menuId){
		List<Object> l =menuDetServiceImpl.getAllObject( PageRequest.of(0,5));
		List<MixMenu> m = new ArrayList<MixMenu>();
		for(Object o:l) {
			Object[] e = (Object[]) o;	
			MixMenu p = new MixMenu();
			p.setMenuItemId( (BigDecimal) e[0]);
			p.setMenuItemDesc((String) e[1]);
			p.setMenuId((BigDecimal) e[2]);
			p.setMCreate((String) e[3]);
			p.setMVisible((String) e[4]);
			p.setMEdit((String) e[5]);
			p.setMQuery((String) e[6]);
			p.setMDelete((String) e[7]);
			m.add(p);
		}
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
	
	@GetMapping("/allGrpMenu")
	public ResponseEntity<?> getAllGrpMenu(){
		Pageable page = PageRequest.of(0,5);
		List<GrpMenuAccess> l = grpMenuAccessServiceImpl.getAll(page);
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	@GetMapping("/getByUserId")
	public ResponseEntity<?> getByUserId(){
		AppUserRole role = appUserServiceImpl.getLoggedInUser().getUsrGrp();
		List<Object> l =menuDetServiceImpl.getByUserId(role.getUsrGrp(),(long) 0);
		List<MixMenu> m = new ArrayList<MixMenu>();
		for(Object o:l) {
			Object[] e = (Object[]) o;	
			MixMenu p = new MixMenu();
			p.setMenuItemId( (BigDecimal) e[0]);
			List<Object> l1 =menuDetServiceImpl.getByUserId(role.getUsrGrp(),p.getMenuItemId().longValue());
			List<MixMenu> m1 = new ArrayList<MixMenu>();
			for(Object o1: l1) {
				Object[] e1 = (Object[]) o1;
				MixMenu p1 = new MixMenu();
				p1.setMenuItemId( (BigDecimal) e1[0]);
				p1.setMenuItemDesc((String) e1[1]);
				p1.setMenuId((BigDecimal) e1[2]);
				p1.setMCreate((String) e1[3]);
				p1.setMVisible((String) e1[4]);
				p1.setMEdit((String) e1[5]);
				p1.setMQuery((String) e1[6]);
				p1.setMDelete((String) e1[7]);
				p1.setMainMenuActionName((String) e1[8]);
				p1.setMainMenuIconName((String) e1[9]);
				m1.add(p1);
			}
			p.setMenuItemDesc((String) e[1]);
			p.setMenuId((BigDecimal) e[2]);
			p.setMCreate((String) e[3]);
			p.setMVisible((String) e[4]);
			p.setMEdit((String) e[5]);
			p.setMQuery((String) e[6]);
			p.setMDelete((String) e[7]);
			p.setMainMenuActionName((String) e[8]);
			p.setMainMenuIconName((String) e[9]);
			p.setSubMenus(m1);
			m.add(p);
		}
		return new ResponseEntity<>(m,HttpStatus.OK);
	}
}
