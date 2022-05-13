package com.realnet.ncso.service.impl1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderItemCompositeKey;
import com.realnet.ncso.entity1.OrderItemEntity;
import com.realnet.ncso.repository1.OrderAttachmentRepository;
import com.realnet.ncso.repository1.OrderItemRepository;
import com.realnet.ncso.service1.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService{
	@Autowired
	private OrderItemRepository orderItemRepo;
	@Autowired
	private OrderAttachmentRepository orderAttachmentRepository;
	public OrderItemServiceImpl(OrderItemRepository orderItemRepo,OrderAttachmentRepository orderAttachmentRepository) {
		super();
		this.orderItemRepo = orderItemRepo;
		this.orderAttachmentRepository=orderAttachmentRepository;
	}
	@Override
	public OrderItemEntity addItem(OrderItemEntity orderItemEntity) {
		System.out.println(orderItemEntity);
		return orderItemRepo.save(orderItemEntity);
	}
	@Override
	public Optional<OrderItemEntity> getOne(Long a,Long b) {
		return orderItemRepo.findByOrderItemCompositeKey(a,b);
	}
	@Override
	public OrderItemEntity updateItem(OrderItemEntity orderItemEntity) {
		return orderItemRepo.save(orderItemEntity);
	}
	@Override
	public void deleteItem(Long a,Long b) {
		orderItemRepo.deleteByCompositeKey(a,b);
	}
	@Override
	public List<OrderItemEntity> findAllItemFromOrderId(Long orderId){
		return orderItemRepo.findAllByOrderId(orderId);
	}
	@Override
	public Optional<OrderItemEntity> getOne(OrderItemCompositeKey orderItemCompositeKey) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Optional<OrderAttachment> findOneAttachment(Long a,Long b) {
		return orderAttachmentRepository.findByOrderAttachmentCompositeKey(a,b);
	}
	@Override
	public OrderAttachment addOneAttachment(OrderAttachment orderAttachment) {
		return orderAttachmentRepository.save(orderAttachment);
	}
	@Override 
	public OrderAttachment updateOneAttachment(OrderAttachment orderAttachment) {
		return orderAttachmentRepository.save(orderAttachment);
	}
	@Override 
	public void deleteOneAttachment(Long a,Long b) {
		orderAttachmentRepository.deleteByCompositeKey(a, b);
	}
	public void deleteItem(Long id) {
		// TODO Auto-generated method stub
		List<OrderItemEntity> items = findAllItemFromOrderId(id);
		for(OrderItemEntity item: items) {
			deleteItem(id, item.getLineId());
		}
	}
	public void deleteAttachment(Long id) {
		// TODO Auto-generated method stub
		List<OrderAttachment> attachments = getAllAttachmentFromOrderId(id);
		for(OrderAttachment attachment: attachments) {
			deleteOneAttachment(id, attachment.getAttachmentId());
		}
		//orderAttachmentRepository.deleteByOrderId(id);
	}
	@Override
	public List<OrderAttachment> getAllAttachmentFromOrderId(Long orderId){
		return orderAttachmentRepository.findAllByOrderId(orderId);
	}
	
	public List<OrderAttachment> updateAllAttachments(List<OrderAttachment> attachments){
		for(OrderAttachment at: attachments) {
			orderAttachmentRepository.save(at);
		}
		return attachments;
	}
	public List<OrderItemEntity> updateAllItems(List<OrderItemEntity> items){
		for(OrderItemEntity it:items) {
			orderItemRepo.save(it);
		}
		return items;
	}
	public List<OrderAttachment> addAllAtthacments(List<OrderAttachment> attachments){
		for(OrderAttachment at: attachments) {
			orderAttachmentRepository.save(at);
		}
		return attachments;
	}
	public List<OrderItemEntity> addAllItems(List<OrderItemEntity> items){
		for(OrderItemEntity it:items) {
			orderItemRepo.save(it);
		}
		return items;
	}
}
