package com.realnet.bi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.bi.entity.Rn_Dashboard_Widgets;
import com.realnet.bi.repository.Rn_dashboard_widget_repository;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.users.entity.User;


@Service
public class Rn_dashboard_widget_serviceImpl implements Rn_dashboard_widget_service{

	
	@Autowired
	private Rn_dashboard_widget_repository widRepo;

	@Override
	public List<Rn_Dashboard_Widgets> getByModule(int id) {
		List<Rn_Dashboard_Widgets> bcf_extractor = widRepo.getByModule(id);
		return bcf_extractor;
	}
	
	
	@Override
	public Rn_Dashboard_Widgets save(Rn_Dashboard_Widgets rn_report_builder) {
		Rn_Dashboard_Widgets savereport = widRepo.save(rn_report_builder);
		return savereport;
	}

	@Override
	public Rn_Dashboard_Widgets saveWidget(Rn_Dashboard_Widgets widget, int moduleId) {
		    
//		    User user = userService.getLoggedInUser();
//			Long userId = user.getUserId();
//			Long accountId = user.getSys_account().getId();

			String widget_name = widget.getWidget_name();
			String widget_description=widget.getWidget_description();
			String chart_type=widget.getChart_type();
			String sql_query=widget.getSql_query();
			String label=widget.getLabel();
			String color_scheme=widget.getColor_scheme();
			
			Rn_Dashboard_Widgets dashWidget=new Rn_Dashboard_Widgets();
			dashWidget.setWidget_name(widget_name);
			dashWidget.setChart_type(chart_type);
			dashWidget.setWidget_description(widget_description);
			dashWidget.setSql_query(sql_query);
			dashWidget.setLabel(label);
			dashWidget.setColor_scheme(color_scheme);
			dashWidget.setModule_id(moduleId);
			
			return save(dashWidget); 
		
		
	}
	
	
}
