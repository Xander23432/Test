package com.realnet.rb.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.realnet.rb.entity.RnTableListDto;
import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.rb.repository.RnTableDtoRepo;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.repository.Rn_tables_Repository;

@Service
public class Rn_rb_tables_serviceImpl implements Rn_rb_tables_service{
	
	@Autowired
	private Rn_report_builder_repository rn_table_repository;
	
	@Autowired
	private Rn_tables_Repository rn_repo;
	
	@Autowired
	private RnTableDtoRepo rn_repoDto;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	
	@Override
	public Rn_report_builder save(Rn_report_builder rn_tables) {
		Rn_report_builder savedTables = rn_table_repository
				.save(rn_tables);
		return savedTables;
	}
	
	@Override
	public List<Rn_rb_Tables> save(List<Rn_rb_Tables> rn_tables) {
		List<Rn_rb_Tables> savedTables = rn_repo
				.saveAll(rn_tables);
		return savedTables;
	}

	@Override
	public List<String> getListOfTables() {
		//Connection con = null;
		//Connection con=null;
		
		String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'realnet'";
		List<String> list=new ArrayList<String>(); 
		try (
				Connection con =DriverManager.getConnection(url,userName,password);
				
			  Statement stmt = con.createStatement()) {
		      ResultSet rs = stmt.executeQuery(query);
		      System.out.println("table list is"+rs);
		      while (rs.next()) 
		      {
		        String coffeeName = rs.getString("table_name");
		        list.add(coffeeName);
		      }
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		return list;
	}
	
	@Override
	public List<String> getListOfColumns(int id) {
		//Connection con = null;
		//Connection con=null;
		
		String query = "SELECT table_allias_name FROM rn_rb_tables_t WHERE report_id="+id+"";
		List<String> list=new ArrayList<String>(); 
		try (
				Connection con =DriverManager.getConnection(url,userName,password);
				
			  Statement stmt = con.createStatement()) {
		      ResultSet rs = stmt.executeQuery(query);
		      while (rs.next()) 
		      {
		        String coffeeName = rs.getString("table_allias_name");
		        list.add(coffeeName);
		      }
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		return list;
	}
	
	@Override
	public List<String> getColumnList(int id) {
		//Connection con = null;
		//Connection con=null;
		
		String query = "SELECT column_name FROM rn_rb_column_t WHERE report_id="+id+"";
		List<String> list=new ArrayList<String>(); 
		try (
				Connection con =DriverManager.getConnection(url,userName,password);
				
			  Statement stmt = con.createStatement()) {
		      ResultSet rs = stmt.executeQuery(query);
		      while (rs.next()) 
		      {
		        String coffeeName = rs.getString("column_name");
		        list.add(coffeeName);
		      }
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		return list;
	}

	
	
	@Override
	public List<String> getColumnAliasList(String name) {
		String query = "SELECT column_name FROM information_schema.columns WHERE TABLE_SCHEMA='realnet' and table_name = '"+name+"' ";
		List<String> list=new ArrayList<String>(); 
		try (
				Connection con =DriverManager.getConnection(url,userName,password);
				
			  Statement stmt = con.createStatement()) {
		      ResultSet rs = stmt.executeQuery(query);
		      while (rs.next()) 
		      {
		        String coffeeName = rs.getString("column_name");
		        list.add(name+"."+coffeeName);
		      }
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		return list;
	}
	
}
