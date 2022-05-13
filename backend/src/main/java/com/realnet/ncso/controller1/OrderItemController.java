package com.realnet.ncso.controller1;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderAttachmentCompositeKey;
import com.realnet.ncso.entity1.OrderItemCompositeKey;
import com.realnet.ncso.entity1.OrderItemEntity;
import com.realnet.ncso.service.impl1.OrderItemServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value="/ncso1")
@Api(tags = {"/ncso"})
public class OrderItemController {
	@Autowired
	private OrderItemServiceImpl orderItemServiceImpl;
	public OrderItemController(OrderItemServiceImpl orderItemServiceImpl) {
		super();
		this.orderItemServiceImpl = orderItemServiceImpl;
	}
	@GetMapping("/findOneItem")
	private ResponseEntity<OrderItemEntity> getItem(@RequestBody OrderItemCompositeKey orderItemCompositeKey){
		Optional<OrderItemEntity> orderItemEntity = orderItemServiceImpl.getOne(orderItemCompositeKey.getOrderId(),orderItemCompositeKey.getLineId());
		return new ResponseEntity<>(orderItemEntity.get(),HttpStatus.OK);
	}
	@PostMapping("/addItem")
	public ResponseEntity<OrderItemEntity> addItem(@RequestBody OrderItemEntity orderItemEntity){
		OrderItemEntity orderItemEntity2 = orderItemServiceImpl.addItem(orderItemEntity);
		return new ResponseEntity<>(orderItemEntity2,HttpStatus.OK);
	}
	
	@PostMapping("/updateItem")
	public ResponseEntity<OrderItemEntity> updateItem(@RequestBody OrderItemEntity orderItemEntity){
		OrderItemEntity orderItemEntity2 = orderItemServiceImpl.updateItem(orderItemEntity);
		return new ResponseEntity<>(orderItemEntity2,HttpStatus.OK);
	}
	@DeleteMapping("/deleteItem")
	public ResponseEntity<?> deleteItem(@RequestBody OrderItemCompositeKey orderItemCompositeKey){
		orderItemServiceImpl.deleteItem(orderItemCompositeKey.getOrderId(),orderItemCompositeKey.getLineId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/findOneItemByOrder/orderId")
	public ResponseEntity<?> findallItemOfOrder(@PathVariable("/orderId") Long orderId){
		List<OrderItemEntity> items=orderItemServiceImpl.findAllItemFromOrderId(orderId);
		return new ResponseEntity<>(items,HttpStatus.OK);
	}

	
	@GetMapping("/findOneAttachmentByOrder/orderId")
	public ResponseEntity<?> findallAttachmentOfOrder(@PathVariable("/orderId") Long orderId){
		List<OrderAttachment> items=orderItemServiceImpl.getAllAttachmentFromOrderId(orderId);
		return new ResponseEntity<>(items,HttpStatus.OK);
	}
	@GetMapping("/findOneAttachment")
	private ResponseEntity<OrderAttachment> getOneAttachment(@RequestBody OrderAttachmentCompositeKey orderAttachmentCompositeKey){
		Optional<OrderAttachment> orderAOptional = orderItemServiceImpl.findOneAttachment(orderAttachmentCompositeKey.getOrderId(),orderAttachmentCompositeKey.getAttachmentId());
		return new ResponseEntity<>(orderAOptional.get(),HttpStatus.OK);
	}
	@PostMapping(path="/addOneAttachment")
	public ResponseEntity<OrderAttachment> addAttachment(@RequestParam Map<String,MultipartFile>  attachmentFile,
			@PathParam(value="orderId") String orderId){
		try {
			Long orderIdL = Long.valueOf(orderId);
			System.out.println(orderIdL);
			Optional<OrderAttachment> attachments = orderItemServiceImpl.findOneAttachment(orderIdL,(long) 1);
			if(attachments.get()!=null) {
				for(Map.Entry<String,MultipartFile> entry : attachmentFile.entrySet()) {
					String aid = entry.getKey().substring(14);
					Long attachmentId = Long.valueOf(aid);
					System.out.println(attachmentId);
					Optional<OrderAttachment> attachments1 = orderItemServiceImpl.findOneAttachment(orderIdL,attachmentId);
					if(attachments1.get()!=null) {
						OrderAttachment attachments2 = attachments1.get();
						attachments2.setAttachment(entry.getValue().getBytes());
						attachments2.setAttachmentType(entry.getValue().getContentType());
						attachments2.setAttachmentFilename(entry.getValue().getName());
						orderItemServiceImpl.updateOneAttachment(attachments2);
					}
			}
			//OrderAttachment orderItemEntity2 = orderItemServiceImpl.updateOneAttachment(attachments.get());
			return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	
	@PostMapping("/testAttachment")
	public ResponseEntity<?> test(@RequestParam("attachmentFile") MultipartFile attachmentFile){
		System.out.println(attachmentFile.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/updateAttachment")
	public ResponseEntity<OrderAttachment> updateAttachment(@RequestBody OrderAttachment orderItemEntity){
		OrderAttachment orderItemEntity2 = orderItemServiceImpl.updateOneAttachment(orderItemEntity);
		return new ResponseEntity<>(orderItemEntity2,HttpStatus.OK);
	}
	@DeleteMapping("/deleteAttachment")
	public ResponseEntity<?> deleteAttachment(@RequestBody OrderAttachmentCompositeKey orderItemCompositeKey){
		System.out.println(orderItemCompositeKey.getAttachmentId());
		orderItemServiceImpl.deleteOneAttachment(orderItemCompositeKey.getOrderId(),orderItemCompositeKey.getAttachmentId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
