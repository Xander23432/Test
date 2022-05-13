package com.realnet.ncso.controller1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.realnet.ncso.entity1.Billing;
import com.realnet.ncso.entity1.CreditNote;
import com.realnet.ncso.entity1.MixOfAll;
import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderDetails;
import com.realnet.ncso.entity1.OrderItemEntity;
import com.realnet.ncso.service.impl1.BillingService;
import com.realnet.ncso.service.impl1.CreditNoteServiceImpl;
import com.realnet.ncso.service.impl1.OrderDetailsServiceImpl;
import com.realnet.ncso.service.impl1.OrderItemServiceImpl;
import com.realnet.users.service1.AppUserServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class OrderDetailsController {
	private OrderDetailsServiceImpl orderDetailsServiceImpl;
	private OrderItemServiceImpl orderItemServiceImpl;
	private BillingService billingService;
	private AppUserServiceImpl appUserServiceImpl;
	private CreditNoteServiceImpl creditNoteServiceImpl;
	@Autowired
	public OrderDetailsController(OrderDetailsServiceImpl orderDetailsServiceImpl
			,OrderItemServiceImpl orderItemServiceImpl,
			BillingService bs,
			AppUserServiceImpl appUserServiceImpl,
			CreditNoteServiceImpl creditNoteServiceImpl) {
		super();
		this.orderDetailsServiceImpl = orderDetailsServiceImpl;
		this.orderItemServiceImpl = orderItemServiceImpl;
		this.billingService=bs;
		this.appUserServiceImpl=appUserServiceImpl;
		this.creditNoteServiceImpl=creditNoteServiceImpl;
	}
	
	@PostMapping(value="/add" )
	public ResponseEntity<OrderDetails> addOrderDetail(@RequestBody MixOfAll orderDetails){
		System.out.println(orderDetails.getOrderItemEntity());
		System.out.println(orderDetails.getOrderDetails());
		System.out.println(orderDetails.getOrderAttachment());
		String createdBy = appUserServiceImpl.getLoggedInUser().getFullName();
		String preparedBy = createdBy;
		String departmentCode = appUserServiceImpl.getLoggedInUser().getDepartmentCode().getDepartmentCode();
		orderDetails.getOrderDetails().setCreatedBy(createdBy);
		orderDetails.getOrderDetails().setUpdatedBy(createdBy);
		orderDetails.getOrderDetails().setPreparedBy(preparedBy);
		orderDetails.getOrderDetails().setDepartmentCode(departmentCode);
		OrderDetails order = orderDetailsServiceImpl.addOrder(orderDetails.getOrderDetails());
		if(orderDetails.getOrderItemEntity() != null) {
			for(int i=0;i<orderDetails.getOrderItemEntity().size();i++) {
				orderDetails.getOrderItemEntity().get(i).setOrderId(order);
				orderItemServiceImpl.addItem(orderDetails.getOrderItemEntity().get(i));
			}
			
		}
		if(orderDetails.getOrderAttachment()!=null) {
			for(int i=0;i<orderDetails.getOrderAttachment().size();i++) {
				orderDetails.getOrderAttachment().get(i).setOrderId(order);
				orderItemServiceImpl.addOneAttachment(orderDetails.getOrderAttachment().get(i));
			}
		}
		
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
	@PostMapping("/update")
	public ResponseEntity<OrderDetails> updateOrderDetail(@RequestBody OrderDetails orderDetails){
		OrderDetails order = orderDetailsServiceImpl.updateOrder(orderDetails);
		return new ResponseEntity<OrderDetails>(order,HttpStatus.OK);
	}
	@GetMapping("/all")
	public ResponseEntity<List<OrderDetails>> getAllOrders(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
		Pageable paging = PageRequest.of(page, size);
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		List<OrderDetails> orderDetails = orderDetailsServiceImpl.findAll1(paging);
		for(OrderDetails o:orderDetails) {
			if(o.getStatus()!=null) {
				String s = appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",o.getStatus());
				if(s!=null) {
					o.setStatus(s);
				}
			}
			o.setCreatedOnFormated(simple.format(o.getCreatedOn()));
			o.setUpdatedOnFormated(simple.format(o.getUpdatedOn()));
			o.setRequestDateFormated(simple.format(o.getRequestDate()));
		}

		return new ResponseEntity<>(orderDetails,HttpStatus.OK);
	}






//	@GetMapping("/all")
//	public ResponseEntity<List<OrderDetails>> getAllOrders(
//			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
//			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
//		Pageable paging = PageRequest.of(page, size);
//		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//		List<OrderDetails> orderDetails = orderDetailsServiceImpl.findAll(paging);
//		orderDetails.forEach(o->o.setCreatedOnFormated(simple.format(o.getCreatedOn())));
//		orderDetails.forEach(o->o.setUpdatedOnFormated(simple.format(o.getUpdatedOn())));
//		orderDetails.forEach(o->o.setRequestDateFormated(simple.format(o.getRequestDate())));
//	//	orderDetails.forEach(Order->Order.setCreatedOnFormated(simple.format(Order.getCreatedOn())));	
//		//orderDetailsServiceImpl.getAllOrders();
////		List<MixOfAll> listMixOfAll = new ArrayList<MixOfAll>();
////		for(OrderDetails order: orderDetails) {
////			System.out.println("hre");
////			List<OrderAttachment> attachments = orderItemServiceImpl.getAllAttachmentFromOrderId(order.getOrderId());
////			List<OrderItemEntity> items = orderItemServiceImpl.findAllItemFromOrderId(order.getOrderId());
////			MixOfAll mixOfAll = new MixOfAll();
////			mixOfAll.setOrderDetails(order);
////			mixOfAll.setOrderAttachment(attachments);
////			mixOfAll.setOrderItemEntity(items);
////			listMixOfAll.add(mixOfAll);
////		}
//		return new ResponseEntity<>(orderDetails,HttpStatus.OK);
//	}
//	
//	@GetMapping("/find/{id}")
//	public ResponseEntity<?> getOne(@PathVariable Long id){
//		Optional<OrderDetails> order = orderDetailsServiceImpl.getOrderById(id);
//		if(order.get()!=null) {
//			List<OrderItemEntity> orderItems = orderItemServiceImpl.findAllItemFromOrderId(id);
//			List<OrderAttachment> orderAttachments = orderItemServiceImpl.getAllAttachmentFromOrderId(id);
//		//	Optional<CreditNote> b = billingService.findByBillingId(order.get().getCreditNote().getBillingId());
//		
//			if(order.get().getCreditNote()!=null) {
//				Optional<Billing> b1 = bs.getOne(order.get().getCreditNote().getBillingId());
//				if(b1.get()!=null) {
//					order.get().setBilling(b1.get());
//				}
//			}
//			orderItems.forEach(o->o.setOrderId(null));
//			orderAttachments.forEach(o->o.setOrderId(null));
//			order.get().setOrderItems(orderItems);
//			order.get().setOrderAttachments(orderAttachments);
////			MixOfAll allEntity = new MixOfAll();
////			allEntity.setOrderDetails(order.get());
////			allEntity.setOrderAttachment(orderAttachments);
////			allEntity.setOrderItemEntity(orderItems);
//			return new ResponseEntity<>(order.get(),HttpStatus.OK);
////			order.get().setOrderAttachments(orderAttachments);
////			order.get().setOrderItems(orderItems);
////			return new ResponseEntity<>(order.get(),HttpStatus.OK);
//	
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
	@GetMapping("/find/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id){
		Optional<OrderDetails> order = orderDetailsServiceImpl.getOrderById(id);
		if(order.get()!=null) {
			DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			try {
				
				String name = orderDetailsServiceImpl.findCustomerName(order.get().getCustomerCode());
				if(name!=null) {
					order.get().setCustomerName(name);
				}
				
				order.get().setCreatedOnFormated(simple.format(order.get().getCreatedOn()));
				order.get().setUpdatedOnFormated(simple.format(order.get().getUpdatedOn()));
				order.get().setRequestDateFormated(simple.format(order.get().getRequestDate()));
				order.get().setApprovedDateFormated(simple.format(order.get().getApproveDate()));
				order.get().setServiceRenderedFromFormated(simple.format(order.get().getServiceRenderedFrom()));
				order.get().setServiceRenderedToFormated(simple.format(order.get().getServiceRenderedTo()));
				order.get().setAtaFormated(simple.format(order.get().getAta()));
				order.get().setAtdFormated(simple.format(order.get().getAtd()));
				List<OrderItemEntity> orderItems = orderItemServiceImpl.findAllItemFromOrderId(id);
				List<OrderAttachment> orderAttachments = orderItemServiceImpl.getAllAttachmentFromOrderId(id);
				if(order.get().getCreditNote()!=null) {
					Optional<Billing> b1 = billingService.getOne(order.get().getCreditNote().getBillingId());
					if(b1.get()!=null) {
						b1.get().setInvoiceDateFormated(simple.format(b1.get().getInvoiceDate()));
						order.get().setBilling(b1.get());
					}
				}
				orderItems.forEach(o->o.setOrderId(null));
				orderAttachments.forEach(o->o.setOrderId(null));
				order.get().setOrderItems(orderItems);
				order.get().setOrderAttachments(orderAttachments);
				order.get().setStatus(appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",order.get().getStatus()));
			}catch(Exception e) {
				List<OrderItemEntity> orderItems = orderItemServiceImpl.findAllItemFromOrderId(id);
				List<OrderAttachment> orderAttachments = orderItemServiceImpl.getAllAttachmentFromOrderId(id);
				if(order.get().getCreditNote()!=null) {
					Optional<Billing> b1 = billingService.getOne(order.get().getCreditNote().getBillingId());
					if(b1.get()!=null) {
						b1.get().setInvoiceDateFormated(simple.format(b1.get().getInvoiceDate()));
						order.get().setBilling(b1.get());
					}
				}
				orderItems.forEach(o->o.setOrderId(null));
				orderAttachments.forEach(o->o.setOrderId(null));
				order.get().setOrderItems(orderItems);
				order.get().setOrderAttachments(orderAttachments);
				order.get().setStatus(appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",order.get().getStatus()));
				return new ResponseEntity<>(order.get(),HttpStatus.OK);
			}
			return new ResponseEntity<>(order.get(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
//	@GetMapping("/find/{id}")
//	public ResponseEntity<?> getOne(@PathVariable Long id){
//		Optional<OrderDetails> order = orderDetailsServiceImpl.getOrderById(id);
//		if(order.get()!=null) {
//			List<OrderItemEntity> orderItems = orderItemServiceImpl.findAllItemFromOrderId(id);
//			List<OrderAttachment> orderAttachments = orderItemServiceImpl.getAllAttachmentFromOrderId(id);
//			MixOfAll allEntity = new MixOfAll();
//			allEntity.setOrderDetails(order.get());
//			allEntity.setOrderAttachment(orderAttachments);
//			allEntity.setOrderItemEntity(orderItems);
//			return new ResponseEntity<>(allEntity,HttpStatus.OK);
////			order.get().setOrderAttachments(orderAttachments);
////			order.get().setOrderItems(orderItems);
////			return new ResponseEntity<>(order.get(),HttpStatus.OK);
//	
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id){
		Optional<OrderDetails> order = orderDetailsServiceImpl.getOrderById(id);
		if(order != null) {
			orderItemServiceImpl.deleteItem(id);
			orderItemServiceImpl.deleteAttachment(id);
			orderDetailsServiceImpl.deleteOrder(id);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/getAllCreditNotes")
	public ResponseEntity<?> getAllCreditNotes(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "15", required = false) Integer size){
		Pageable p = PageRequest.of(page,size);
		List<CreditNote> c = creditNoteServiceImpl.getAll(p);
		return new ResponseEntity<>(c,HttpStatus.OK);
	}
}
