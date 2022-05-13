package com.realnet.ncso.repository1;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.BillingItemDetail;
import com.realnet.ncso.entity1.BillingItemDetailCompositeKey;

@Repository
public interface BillingItemDetailRepository extends JpaRepository<BillingItemDetail,BillingItemDetailCompositeKey>{
	@Query(value="SELECT\n"
			+ "       DISTINCT(so.GKEY) \n"
			+ "    FROM\n"
			+ "        BILLING_ITEM_DETAIL bid,\n"
			+ "        SERVICE_ORDERS so \n"
			+ "    WHERE\n"
			+ "        bid.BILLING_ID = ?1\n"
			+ "        AND so.SGKEY =bid.SOURCE_ID1;",nativeQuery=true)
	List<Object> findAllCustom(Long billingId);
}
