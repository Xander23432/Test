package com.realnet.users.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.fnd.entity.Rn_Who_Columns;
import com.realnet.session.entity.AboutWork;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "sys_account")
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "usertbl") 
public class User extends Rn_Who_Columns { // implements Comparable<User>
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String firstName;

	private String lastName;

	private String fullName;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String username;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	// added = 7.12.20
	@Column(name = "menu_group_id")
	private int menu_group_id;
	
	@Column(name = "status")
	private String status; // for invitation
	
	@Column(name = "department")
	private String department;

	@Column(name = "about")
	private String about;
	@Column(name = "photos")
	private String photos;

	private String role;

	// private String company;

	@JsonIgnore
	private int securityProviderId;

	@JsonIgnore
	private int defaultCustomerId;

	@JsonIgnore
	private String phone;

	@JsonIgnore
	private String address1;

	@JsonIgnore
	private String address2;
	@JsonIgnore
	private String country;

	@JsonIgnore
	private String postal;

	@JsonIgnore
	@Column(name = "is_active")
	private boolean enabled;
	@JsonIgnore
	private boolean isBlocked;
	@JsonIgnore
	private String secretQuestion;
	@JsonIgnore
	private String secretAnswer;
	@JsonIgnore
	private boolean enableBetaTesting;
	@JsonIgnore
	private boolean enableRenewal;
	
	@JsonIgnore
	private String pancard;
	
    @JsonIgnore
	private String working;

    @JsonIgnore
   	private String managing_work;
    
    @Column(name = "checknumber")
    private Long checknumber;


	@JsonManagedReference
	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "USER_ROLES", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> roles;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID")
	@JsonBackReference
	private Sys_Accounts sys_account;


//	public void setChecknumber(long nextLong) {
//		this.checknumber=nextLong;
//		
//	}
	
//	@Column(name = "Accounts_id")
//	private int account_id;
	

}