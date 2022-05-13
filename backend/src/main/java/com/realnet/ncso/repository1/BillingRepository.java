package com.realnet.ncso.repository1;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>{
	Optional<Billing> findByInvoiceNo(String invoiceNo);
}
