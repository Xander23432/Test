package com.realnet.ncso.repository1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.EbsInvoiceType;
import com.realnet.ncso.entity1.OrderDetails;

@Repository
public interface EbsInvoiceTypeRepository extends JpaRepository<EbsInvoiceType, String>{
	@Query(value = "select so.order_id, so.request_date, it.description, so.customer_code, c.customer_name, so.vessel_code, so.vessel_name, \n"
			+ "so.in_voyage, so.out_voyage, so.prepared_by, so.status \n"
			+ "from nc_service_order so, ebs_invoice_type it,ebs_customer c \n"
			+ "where so.status <> 'C' \n"
			+ "and so.invoice_type = it.invoice_type\n"
			+ "and so.customer_code = c.customer_code\n"
			+ "order by so.order_id desc",
			countQuery= "select count(*) "
					+ "from nc_service_order so, ebs_invoice_type it,ebs_customer c \n"
					+ "where so.status <> 'C' \n"
					+ "and so.invoice_type = it.invoice_type\n"
					+ "and so.customer_code = c.customer_code\n"
					+ "order by so.order_id desc",
					nativeQuery=true)
	Page<Object> getApproved(Pageable page);
	
}
