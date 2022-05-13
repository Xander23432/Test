package com.realnet.ncso.repository1;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.CreditNote;
import com.realnet.ncso.entity1.CreditNoteCompositeKey;

@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, CreditNoteCompositeKey>{
	@Query(value = "select * from credit_note where dispute_id =?1",nativeQuery=true)
	public Optional<CreditNote> findByDisputeId(Long disputeId);

	public Optional<CreditNote> findByBillingId(Long billingId);
}
