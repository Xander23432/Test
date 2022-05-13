package com.realnet.ncso.repository1;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.BillingAttachment;
import com.realnet.ncso.entity1.BillingAttachmentCompositeKey;

@Repository
public interface BillingAttachmentRepo extends JpaRepository<BillingAttachment,BillingAttachmentCompositeKey>{
	@Query(value="SELECT * FROM BILLING_ATTACHMENT WHERE BILLING_ID = ?1",nativeQuery = true)
	List<BillingAttachment> findByBillingId(Long billingId);
}
