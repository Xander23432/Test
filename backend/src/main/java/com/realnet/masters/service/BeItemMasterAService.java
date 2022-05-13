package com.realnet.masters.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.masters.entity.AuditItemReport;
import com.realnet.masters.entity.BeItemMasterA;
import com.realnet.masters.repository.BeItemMasterARepo;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

import antlr.StringUtils;

@Service
public class BeItemMasterAService {
	private BeItemMasterARepo beItemMasterARepo;
	private AppUserServiceImpl appUserServiceImpl;
	@Autowired
	public BeItemMasterAService(BeItemMasterARepo beItemMasterARepo,
			AppUserServiceImpl appUserServiceImpl) {
		super();
		this.beItemMasterARepo = beItemMasterARepo;
		this.appUserServiceImpl=appUserServiceImpl;
	}
	public List<BeItemMasterA> getAll(Pageable p){
		return beItemMasterARepo.findAll(p).getContent();
	}
	public BeItemMasterA getOne(Long Id) {
		return beItemMasterARepo.findById(Id).orElse(null);
	}
	public List<AuditItemReport> getAudit(Pageable p) {
		try {
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("17/6/2021");
			Date date2=new SimpleDateFormat("dd/MM/yyyy").parse("20/1/2022");
			List<BeItemMasterA> a = beItemMasterARepo.findBychangedDateBetween(date1, date2);
			System.out.println(a.size());
			List<AuditItemReport> l = new ArrayList<AuditItemReport>();
			DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			a.forEach(o->{
				Map<String,String[]> m = checkChanges(o);
				for (Map.Entry<String,String[]> entry : m.entrySet()) {
					AuditItemReport temp = new AuditItemReport();
					temp.setChangedCoulumn(entry.getKey());
					temp.setOriginalValue(entry.getValue()[0]);
					temp.setChangedValue(entry.getValue()[1]);
					temp.setTableName("BE_ITEM_MASTER");
					temp.setReferance("ItemCode: "+o.getNewItemCode());
					temp.setChangedDate(simple.format(o.getChangedDate()));
					AppUser u = appUserServiceImpl.getById(o.getChangedBy()).orElse(null);
					if(u!=null) {
						temp.setChangedBy(u.getUsername());
					}
					l.add(temp);
				}
			});
			return l;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
	public List<AuditItemReport> getAuditCustom(String id,String d1,String d2){
		try {
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(d1);
			Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(d2);
			List<BeItemMasterA> a;
			if(id!=null) {
				Long i = Long.parseLong(id);
				a= beItemMasterARepo.findCustom(i, date1, date2);
			}else {
				a=beItemMasterARepo.findBychangedDateBetween(date1, date2);
			}
			List<AuditItemReport> l = new ArrayList<AuditItemReport>();
			DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			a.forEach(o->{
				Map<String,String[]> m = checkChanges(o);
				for (Map.Entry<String,String[]> entry : m.entrySet()) {
					AuditItemReport temp = new AuditItemReport();
					temp.setChangedCoulumn(entry.getKey());
					temp.setOriginalValue(entry.getValue()[0]);
					temp.setChangedValue(entry.getValue()[1]);
					temp.setTableName("BE_ITEM_MASTER");
					temp.setReferance("ItemCode: "+o.getNewItemCode());
					temp.setChangedDate(simple.format(o.getChangedDate()));
					AppUser u = appUserServiceImpl.getById(o.getChangedBy()).orElse(null);
					if(u!=null) {
						temp.setChangedBy(u.getUsername());
					}
					l.add(temp);
				}
			});
			return l;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
	private Map<String,String[]> checkChanges(BeItemMasterA o) {
		Map<String,String[]> m = new HashMap<>();
		if(o.getOldAccountCode()!=null) {
			String s[]= new String[] {o.getOldAccountCode(),o.getNewAccountCode()};
			m.put("AccountCode",s);
		}
		if(o.getOldActiveFlag()!=null) {
			String s[]=new String[] {o.getOldActiveFlag(),o.getNewActiveFlag()};
			m.put("ActiveFlag",s);
		}
		if(o.getOldCostCentre()!=null) {
			String s[]=new String[] {o.getOldCostCentre(),o.getNewCostCentre()};
			m.put("CostCentre",s);
		}
		if(o.getOldDescription()!=null) {
			String s[]=new String[] {o.getOldDescription(),o.getNewDescription()};
			m.put("Description",s);
		}
		if(o.getOldItemCode()!=null) {
			String s[]=new String[] {o.getOldItemCode(),o.getNewItemCode()};
			m.put("ItemCode",s);
		}
		if(o.getOldTaxCode()!=null) {
			String s[]=new String[] {o.getOldTaxCode(),o.getNewTaxCode()};
			m.put("TaxCode",s);
		}
		if(o.getOldUom()!=null) {
			String s[]=new String[] {o.getOldUom(),o.getNewUom()};
			m.put("Uom",s);
		}
		return m;
	}
}
