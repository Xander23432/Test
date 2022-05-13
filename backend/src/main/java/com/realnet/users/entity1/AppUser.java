package com.realnet.users.entity1;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SEC_USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="user_id")
	@GeneratedValue(generator = "SecUsers_gen")
	@SequenceGenerator(name="SecUsers_gen", sequenceName="sec_users_sequencs",initialValue = 10007300, allocationSize = 1)
	private Long userId;
	@Column(name="user_name")
	private String username;
	private String userPassw;
	private String title;
	private String shortName;
	private String fullName;
	private Date expiryDate;
	private String daysMth;
	private Long noDaysMth;
	private String status;
	private String changePassw;
	private String createby;
	private Date createdate;
	private String updateby;
	private Date updatedate;
	@ManyToOne
	@JoinColumn(name = "usr_grp")
	private AppUserRole usrGrp;
	private String langCode;
	private String firstLogin;
	@ManyToOne
	@JoinColumn(name="position_code")
	private AppUserPosition positionCode;
	@ManyToOne
	@JoinColumn(name="department_code")
	private AppUserDepartment departmentCode;
	private String email;
	private String notification;
	private Long customerId;
	private String password1;
	private String password2;
	private String password3;
	private String password4;
	private Long pwdChangedCnt;
	private Date lastPwdChangedDate;
	@Transient
	private String customerNumer;
	@Transient
	private String positionCodeString;
	@Transient
	private String departmentCodeString;
	@Transient 
	private Long usrGrpId;
	@Transient 
	private String confirmPassword;
	@Transient
	private String usrGrpName;
	@Transient
	private StringBuilder totalLogInfo;
	
}
