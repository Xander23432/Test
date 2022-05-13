package com.realnet.users.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.realnet.users.entity.Role;
import com.realnet.users.entity.User;

public interface RoleService {
	
	public List<Role> getRoles();
	
	public Page<Role> getAll(Pageable page);
	
	public Role getRoleById(Long id);

	ResponseEntity<Object> addRole(Role role);

	ResponseEntity<Object> deleteRole(Long id);
	 
	ResponseEntity<Object> updateRole(Long id, Role role);
	
	// add or remove a Role on a user
	 public User addRole(Long id, Long roleId);
	 public User removeRole(Long id, Long roleId);
}
