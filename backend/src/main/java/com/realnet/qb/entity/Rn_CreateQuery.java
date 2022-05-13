package com.realnet.qb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.realnet.fnd.entity.Rn_Who_AccId_Column;

import java.util.Date;

@Entity
@Table(name = "RN_CREATE_QUERY_T")
public class Rn_CreateQuery extends Rn_Who_AccId_Column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "PROJECT_ID")
	private long projectId;

	@Column(name = "TABLE_NAME")
	private String tableName;
	
	@Column(name = "CREATE_QUERY")
	private String createQuery;

	@Column(name = "IS_BUILD")
	private boolean build;

	@Column(name = "DATA")
	private boolean data;

	public Rn_CreateQuery() {
		super();
	}

	public Rn_CreateQuery(Integer id, long projectId, String tableName, String createQuery, boolean build,
			boolean data) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.tableName = tableName;
		this.createQuery = createQuery;
		this.build = build;
		this.data = data;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCreateQuery() {
		return createQuery;
	}

	public void setCreateQuery(String createQuery) {
		this.createQuery = createQuery;
	}

	public boolean isBuild() {
		return build;
	}

	public void setBuild(boolean build) {
		this.build = build;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}
	
}