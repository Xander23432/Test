package com.realnet.ncso.repository1;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderAttachmentCompositeKey;

@Repository
@Transactional
public interface OrderAttachmentRepository extends JpaRepository<OrderAttachment, OrderAttachmentCompositeKey>{
	@Query(value="SELECT * FROM NC_SERVICE_ORDER_ATTACHMENT WHERE ORDER_ID=?1",nativeQuery=true)
	public List<OrderAttachment> findAllByOrderId(Long orderId);
	
	@Query(value = "SELECT * FROM NC_SERVICE_ORDER_ATTACHMENT WHERE ORDER_ID = ?1 AND ATTACHMENT_ID = ?2", nativeQuery = true)
	Optional<OrderAttachment> findByOrderAttachmentCompositeKey(Long a, Long b);
	@Query(value= "DELETE FROM NC_SERVICE_ORDER_ATTACHMENT WHERE ORDER_ID=?1 AND ATTACHMENT_ID=?2",nativeQuery=true)
	public void deleteByCompositeKey(Long a,Long b);
	@Query(value= "DELETE FROM NC_SERVICE_ORDER_ATTACHMENT WHERE ORDER_ID=?1",nativeQuery=true)
	void deleteByOrderId(Long id);
}
