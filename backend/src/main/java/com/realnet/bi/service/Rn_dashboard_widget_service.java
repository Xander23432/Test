package com.realnet.bi.service;

import java.util.List;

import com.realnet.bi.entity.Rn_Dashboard_Widgets;
import com.realnet.rb.entity.Rn_report_builder;

public interface Rn_dashboard_widget_service {

	List<Rn_Dashboard_Widgets> getByModule(int id);
	Rn_Dashboard_Widgets saveWidget(Rn_Dashboard_Widgets widget, int moduleId);
	Rn_Dashboard_Widgets save(Rn_Dashboard_Widgets rn_report_builder);
}
