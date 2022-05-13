package com.realnet.ncso.service1;

import java.util.List;
import java.util.Optional;

import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderItemCompositeKey;
import com.realnet.ncso.entity1.OrderItemEntity;

public interface OrderItemService {

	OrderItemEntity addItem(OrderItemEntity orderItemEntity);

	Optional<OrderItemEntity> getOne(OrderItemCompositeKey orderItemCompositeKey);

	Optional<OrderItemEntity> getOne(Long a, Long b);

	Optional<OrderAttachment> findOneAttachment(Long a,Long b);

	OrderItemEntity updateItem(OrderItemEntity orderItemEntity);

	void deleteItem(Long a, Long b);

	OrderAttachment addOneAttachment(OrderAttachment orderAttachment);

	OrderAttachment updateOneAttachment(OrderAttachment orderAttachment);

	void deleteOneAttachment(Long a, Long b);

	List<OrderAttachment> getAllAttachmentFromOrderId(Long orderId);

	List<OrderItemEntity> findAllItemFromOrderId(Long orderId);

}
