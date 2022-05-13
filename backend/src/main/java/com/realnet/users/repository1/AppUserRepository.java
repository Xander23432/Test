package com.realnet.users.repository1;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.users.entity1.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long>{
	Optional<AppUser> findByUsername(String username);

	AppUser findByEmail(String email);

	Optional<AppUser> findByUsernameAndUserPassw(String username, String userPassw);

	Boolean existsByEmail(String email);
	@Query(value="SELECT customer_number FROM  be_cust_master  WHERE customer_id = ?1",
			countQuery = "SELECT count(*) FROM  be_cust_master  WHERE customer_id = ?1",
			nativeQuery=true)
	String getCustomerNumber(BigDecimal customerId);
	
	@Query(value="select refcodedesc from refcodedet where reftype = ?1 and refcode = ?2",
			countQuery="select refcodedesc from refcodedet where reftype = ?1 and refcode = ?2",
			nativeQuery=true)
	String getStatus(String tableName,String code);
}
