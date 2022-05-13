package com.realnet.ncso.repository1;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.BillingItem;
import com.realnet.ncso.entity1.BillingItemCompositeKey;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem,BillingItemCompositeKey>{
	@Query(value="SELECT * FROM BILLING_ITEM bi WHERE bi.BILLING_ID = ?1",nativeQuery=true)
	List<BillingItem> findByBillingId(Long billingId);
}
