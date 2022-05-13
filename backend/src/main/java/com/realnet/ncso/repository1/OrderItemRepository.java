package com.realnet.ncso.repository1;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.ncso.entity1.OrderItemCompositeKey;
import com.realnet.ncso.entity1.OrderItemEntity;

@Repository
@Transactional
public interface OrderItemRepository extends JpaRepository<OrderItemEntity,OrderItemCompositeKey>{
	@Query(value="SELECT * FROM NC_SERVICE_ORDER_ITEM WHERE ORDER_ID=?1" ,nativeQuery=true)
	public List<OrderItemEntity> findAllByOrderId(Long orderId);
	
	@Query(value = "SELECT * FROM NC_SERVICE_ORDER_ITEM WHERE ORDER_ID = ?1 AND LINE_ID = ?2", nativeQuery = true)
	public Optional<OrderItemEntity> findByOrderItemCompositeKey(Long orderId,Long lineId);
	@Query(value= "DELETE FROM NC_SERVICE_ORDER_ITEM WHERE ORDER_ID=?1 AND LINE_ID=?2",nativeQuery=true)
	public void deleteByCompositeKey(Long a,Long b);
	@Query(value= "DELETE FROM NC_SERVICE_ORDER_ITEM WHERE ORDER_ID=?1",nativeQuery=true)
	public void deleteByOrderId(Long id);
}
