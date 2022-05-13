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
import com.realnet.masters.entity.BeLeaseMasterA;
import com.realnet.masters.repository.BeLeaseMasterARepo;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@Service
public class BeLeaseMasterASevice {
	private BeLeaseMasterARepo beLeaseMasterARepo;
	private AppUserServiceImpl appUserServiceImpl;
	@Autowired
	public BeLeaseMasterASevice(BeLeaseMasterARepo beLeaseMasterARepo,
			AppUserServiceImpl appUserServiceImpl) {
		super();
		this.beLeaseMasterARepo = beLeaseMasterARepo;
		this.appUserServiceImpl=appUserServiceImpl;
	}
	public List<BeLeaseMasterA> getAll(Pageable p){
		return beLeaseMasterARepo.findAll(p).getContent();
	}
	public BeLeaseMasterA getOne(Long id) {
		return beLeaseMasterARepo.findById(id).orElse(null);
	}
	public List<AuditItemReport> getAudit(Pageable p) {
		try {
			Date date1=new SimpleDateFormat("dd/MM/yyyy").parse("17/6/2021");
			Date date2=new SimpleDateFormat("dd/MM/yyyy").parse("20/1/2022");
			List<BeLeaseMasterA> a = beLeaseMasterARepo.findBychangedDateBetween(date1, date2);
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
					temp.setTableName("BE_LEASE_MASTER");
					temp.setReferance("LeaseId: "+o.getLeaseId());
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
			List<BeLeaseMasterA> a;
			if(id!=null) {
				Long i = Long.parseLong(id);
				a= beLeaseMasterARepo.findCustom(i, date1, date2);
			}else {
				a=beLeaseMasterARepo.findBychangedDateBetween(date1, date2);
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
					temp.setTableName("BE_LEASE_MASTER");
					temp.setReferance("LeaseId: "+o.getLeaseId());
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
	private Map<String,String[]> checkChanges(BeLeaseMasterA o) {
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Map<String,String[]> m = new HashMap<>();
		if(o.getOldLeaseType()!=null) {
			String s[]= new String[] {o.getOldLeaseType(),o.getNewLeaseType()};
			m.put("LeaseType",s);
		}
		if(o.getOldLocationCode()!=null) {
			String s[]=new String[] {o.getOldLocationCode(),o.getNewLocationCode()};
			m.put("LocationCode",s);
		}
		if(o.getOldLeaseStatus()!=null) {
			String s[]=new String[] {o.getOldLeaseStatus(),o.getNewLeaseStatus()};
			m.put("LeaseStatus",s);
		}
		if(o.getOldCustomerId()!=null) {
			String s[]=new String[] {o.getOldCustomerId().toString(),o.getNewCustomerId().toString()};
			m.put("CustomerId",s);
		}
		if(o.getOldStartDate()!=null) {
			String s[]=new String[] {simple.format( o.getOldStartDate()),simple.format( o.getNewStartDate())};
			m.put("StartDate",s);
		}
		if(o.getOldEndDate()!=null) {
			String s[]=new String[] {simple.format(o.getOldEndDate()),simple.format(o.getNewEndDate())};
			m.put("EndDate",s);
		}
		if(o.getOldTerminatedDate()!=null) {
			String s[]=new String[] {simple.format(o.getOldTerminatedDate()),simple.format(o.getNewTerminatedDate())};
			m.put("TerminatedDate",s);
		}
		if(o.getOldFrequencyCode()!=null) {
			String s[]=new String[] {o.getOldFrequencyCode(),o.getNewFrequencyCode()};
			m.put("FrequencyCode",s);
		}
		if(o.getOldScheduleDay()!=null) {
			String s[]=new String[] {o.getOldScheduleDay().toString(),o.getNewScheduleDay().toString()};
			m.put("ScheduleDay",s);
		}
		if(o.getOldArea()!=null) {
			String s[]=new String[] {o.getOldArea().toString(),o.getNewArea().toString()};
			m.put("Area",s);
		}
		if(o.getOldRate()!=null) {
			String s[]=new String[] {o.getOldRate().toString(),o.getNewRate().toString()};
			m.put("Rate",s);
		}
		if(o.getOldActualAmount()!=null) {
			String s[]=new String[] {o.getOldActualAmount().toString(),o.getNewActualAmount().toString()};
			m.put("ActualAmount",s);
		}
		if(o.getOldPaymentTermId()!=null) {
			String s[]=new String[] {o.getOldPaymentTermId().toString(),o.getNewPaymentTermId().toString()};
			m.put("PaymentTermId",s);
		}
		if(o.getOldTaxCodeId()!=null) {
			String s[]=new String[] {o.getOldTaxCodeId().toString(),o.getNewTaxCodeId().toString()};
			m.put("TaxCodeId",s);
		}
		if(o.getOldTaxIncluded()!=null) {
			String s[]=new String[] {o.getOldTaxIncluded(),o.getNewTaxIncluded()};
			m.put("TaxIncluded",s);
		}
		if(o.getOldRevenueAccount()!=null) {
			String s[]=new String[] {o.getOldRevenueAccount(),o.getNewRevenueAccount()};
			m.put("RevenueAccount",s);
		}
		if(o.getOldReceivablesAccount()!=null) {
			String s[]=new String[] {o.getOldReceivablesAccount(),o.getNewReceivablesAccount()};
			m.put("RecievablesAccount",s);
		}
		return m;
	}
}
