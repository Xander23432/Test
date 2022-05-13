package com.realnet.ncso.controller1;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realnet.ncso.entity1.Billing;
import com.realnet.ncso.entity1.CreditNote;
import com.realnet.ncso.entity1.OrderAttachment;
import com.realnet.ncso.entity1.OrderDetails;
import com.realnet.ncso.entity1.OrderItemEntity;
import com.realnet.ncso.entity1.Paging;
import com.realnet.ncso.service.impl1.BillingService;
import com.realnet.ncso.service.impl1.OrderDetailsServiceImpl;
import com.realnet.ncso.service.impl1.OrderItemServiceImpl;
import com.realnet.users.service1.AppUserServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class DisputeController {
	private BillingService billingService;
	private OrderDetailsServiceImpl orderDetailsServiceImpl;
	private OrderItemServiceImpl orderItemServiceImpl;
	private AppUserServiceImpl appUserServiceImpl;
	private CacheManager cacheManager;
	@Autowired
	public DisputeController(BillingService billingService, OrderDetailsServiceImpl orderDetailsServiceImpl,
			OrderItemServiceImpl orderItemServiceImpl,
			AppUserServiceImpl appUserServiceImpl,CacheManager cacheManager) {
		super();
		this.billingService = billingService;
		this.orderDetailsServiceImpl = orderDetailsServiceImpl;
		this.orderItemServiceImpl = orderItemServiceImpl;
		this.appUserServiceImpl=appUserServiceImpl;
		this.cacheManager=cacheManager;
	}
	@GetMapping("/getOrdersOfCurrentUser")
	public ResponseEntity<?> getOrdersOfCurrentUsr(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "15", required = false) Integer size){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Pageable pageable = PageRequest.of(page,size);
		List<OrderDetails> l = orderDetailsServiceImpl.getAllOrderOfCurrentUser(pageable,appUserServiceImpl.getLoggedInUser().getUsername());
		l.forEach(o1->{
			String name = orderDetailsServiceImpl.findCustomerName(o1.getCustomerCode());
			if(name!=null) {
				o1.setCustomerName(name);
			}
			
		});
		l.forEach(o1->o1.setStatus(appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",o1.getStatus())));
		l.forEach(o1->o1.setRequestDateFormated(simple.format(o1.getRequestDate())));

		return new ResponseEntity<>(l,HttpStatus.OK);
		}
	
	@GetMapping("/getAllByDeptByGrpLevel")
	public ResponseEntity<?> getAllOrdersFromDepartment(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Pageable paging = PageRequest.of(page,size);
		Long grpLevel = appUserServiceImpl.getLoggedInUser().getUsrGrp().getGroupLevel();
		String departmentCode = appUserServiceImpl.getLoggedInUser().getDepartmentCode().getDepartmentCode();
		List<OrderDetails> o = orderDetailsServiceImpl.getAllByDeptAndByGrpLevel(paging, grpLevel, departmentCode);
		
		
		o.forEach(o1->{
			String name = orderDetailsServiceImpl.findCustomerName(o1.getCustomerCode());
			if(name!=null) {
				o1.setCustomerName(name);
			}
			
		});
		o.forEach(o1->o1.setStatus(appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",o1.getStatus())));
		o.forEach(o1->o1.setRequestDateFormated(simple.format(o1.getRequestDate())));
		return new ResponseEntity<>(o,HttpStatus.OK);
	}
	
	@GetMapping("/getBilling/{billingId}")
	public ResponseEntity<?> getOneBilling(@PathVariable("billingId") Long billingId){
		Optional<Billing> b = billingService.getOne(billingId);	
		return new ResponseEntity<>(b.get(),HttpStatus.OK);
	}
	@GetMapping("/getAllBilling")
	public ResponseEntity<?> getAllBilling(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size){
		Pageable paging = PageRequest.of(page, size);
		List<Billing> b = billingService.getAllBilling(paging);
		return new ResponseEntity<>(b,HttpStatus.OK);
	}
	

//	@GetMapping("/getAllCreditNote/{page}")
//	public ResponseEntity<?> getAllCreditNote(
//			@PathVariable int page,
//			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size){
//		Pageable paging = PageRequest.of(page, 10);
//		List t= orderDetailsServiceImpl.findAll1(paging);
//		Page<OrderDetails> p = (Page<OrderDetails>) t.get(0);		
//		List<OrderDetails> o = (List<OrderDetails>) t.get(1);
//		for(OrderDetails order: o) {
//
//			String createdBy = appUserServiceImpl.getLoggedInUser().getFullName();
//			DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//			order.setCreatedBy(createdBy);
//			order.setCreatedOnFormated(simple.format(order.getCreatedOn()));
//			order.setUpdatedOnFormated(simple.format(order.getUpdatedOn()));
//			order.setRequestDateFormated(simple.format(order.getRequestDate()));
//			if(order.getCreditNote()!=null) {
//				if(order.getCreditNote().getBillingId()!=null) {
//					Optional<Billing> b = billingService.getOne(order.getCreditNote().getBillingId());
//					if(b.get()!=null) {
//						b.get().setInvoiceDateFormated(simple.format(b.get().getInvoiceDate()));
//						b.get().setConfirmDateFormated(simple.format(b.get().getConfirmDate()));
//						b.get().setPostDateFormated(simple.format(b.get().getPostDate()));
//						b.get().setCreatedOnFormated(simple.format(b.get().getCreatedOn()));
//						b.get().setUpdatedOnFormated(simple.format(b.get().getUpdatedOn()));
//						order.setBilling(b.get());
//					}
//				}
//				
//			}
//		}
//		Object obj = new Object() {
//			private List<OrderDetails> content = o;
//			private int totalPages = p.getTotalPages();
//			private int currentPage = p.getNumber();
//			private long totalElements = p.getTotalElements();
//			private int pageSize = p.getSize();
//			public List<OrderDetails> getContent() {
//				return content;
//			}
//			public void setContent(List<OrderDetails> content) {
//				this.content = content;
//			}
//			public int getTotalPages() {
//				return totalPages;
//			}
//			public void setTotalPages(int totalPages) {
//				this.totalPages = totalPages;
//			}
//			public int getCurrentPage() {
//				return currentPage;
//			}
//			public void setCurrentPage(int currentPage) {
//				this.currentPage = currentPage;
//			}
//			
//			public long getTotalElements() {
//				return totalElements;
//			}
//			public void setTotalElements(long totalElements) {
//				this.totalElements = totalElements;
//			}
//			public int getPageSize() {
//				return pageSize;
//			}
//			public void setPageSize(int pageSize) {
//				this.pageSize = pageSize;
//			}
//		};
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
	
	@GetMapping("/getAllCreditNote")
	public ResponseEntity<?> getAllCreditCustom(@RequestParam(value="search",required = false) String search){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		List<Object> l;
		if(search==null) {
			
			l =  orderDetailsServiceImpl.getAllCustomCreditNote("");
		}else {
			//search = "%"+search+"%";
			l= orderDetailsServiceImpl.getAllCustomCreditNote(search);
		}
		

		List<OrderDetails> orders = new ArrayList<>();
		for(Object o:l) {
			Object[] e = (Object[]) o;
			OrderDetails o1 = new OrderDetails();
			CreditNote cn = new CreditNote();
			Billing b = new Billing();
			BigDecimal b1 = (BigDecimal) e[0];
			if(b1!=null) {
				cn.setDisputeId(b1.longValue());
			}
		
			cn.setCreditNoteNo((String) e[1]);
			b1=(BigDecimal) e[2];
			if(b1!=null) {
				cn.setBillingId(b1.longValue());
			}
			
			b.setBillingId(b1.longValue());
			b.setInvoiceNo((String) e[3]);
			b.setInvoiceDate((Date) e[4]);
			if(b.getInvoiceDate()!=null) {
				b.setInvoiceDateFormated(simple.format(b.getInvoiceDate()));
			}
			
			o1.setInvoiceType((String) e[5]);
			o1.setCustomerOrderNo((String) e[6]);
			o1.setServiceRenderedFrom((Date) e[7]);
			if(o1.getServiceRenderedFrom()!=null) {
				o1.setServiceRenderedFromFormated(simple.format(o1.getServiceRenderedFrom()));
			}
			
			o1.setServiceRenderedTo((Date) e[8]);
			if(o1.getServiceRenderedTo()!=null) {
				o1.setServiceRenderedToFormated(simple.format(o1.getServiceRenderedTo()));
			}
			
			o1.setServiceRequestBy((String) e[9]);
			o1.setCustomerCode((String) e[10]);
			o1.setCustomerName((String) e[11]);
			o1.setPoNumber((String) e[12]);
			o1.setVesselCode((String) e[13]);
			o1.setVesselName((String) e[14]);
			o1.setInVoyage((String) e[15]);
			o1.setOutVoyage((String) e[16]);
			o1.setLineCode((String) e[17]);
			o1.setCallNo((String) e[18]);
			b1=(BigDecimal) e[19];
			if(b1!=null) {
				o1.setLoa(b1);
			}
			
			o1.setLoaUom((String) e[20]);
			b1=(BigDecimal) e[21];
			if(b1!=null) {
				o1.setGt(b1);
			}
			
			o1.setAta((Date) e[22]);
			if(o1.getAta()!=null) {
				o1.setAtaFormated(simple.format(o1.getAta()));
			}
			
			o1.setAtd((Date) e[23]);
			if(o1.getAtd()!=null) {
				o1.setAtdFormated(simple.format(o1.getAtd()));
			}
			
			o1.setLocation((String) e[24]);
			o1.setRemarks((String) e[25]);
			o1.setCreditNote(cn);
			o1.setBilling(b);
			orders.add(o1);
		}
		Object obj = new Object() {
			private List<OrderDetails> content = orders;

			public List<OrderDetails> getContent() {
				return content;
			}
			public void setContent(List<OrderDetails> content) {
				this.content = content;
			}

		};
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	@GetMapping("/getAllCreditNoteWithoutPagination")
	public ResponseEntity<?> getWithoutPage(){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//		Pageable p1= PageRequest.of(page,10);
		//Page<Object> p = orderDetailsServiceImpl.getAllCustomCreditNote(p1);
		List<Object> l  =orderDetailsServiceImpl.getAllCreditNoteWithoutPagination();
		List<OrderDetails> orders = new ArrayList<>();
		for(Object o:l) {
			Object[] e = (Object[]) o;
			OrderDetails o1 = new OrderDetails();
			CreditNote cn = new CreditNote();
			Billing b = new Billing();
			BigDecimal b1 = (BigDecimal) e[0];
			if(b1!=null) {
				cn.setDisputeId(b1.longValue());
			}
		
			cn.setCreditNoteNo((String) e[1]);
			b1=(BigDecimal) e[2];
			if(b1!=null) {
				cn.setBillingId(b1.longValue());
			}
			
			b.setBillingId(b1.longValue());
			b.setInvoiceNo((String) e[3]);
			b.setInvoiceDate((Date) e[4]);
			if(b.getInvoiceDate()!=null) {
				b.setInvoiceDateFormated(simple.format(b.getInvoiceDate()));
			}
			
			o1.setInvoiceType((String) e[5]);
			o1.setCustomerOrderNo((String) e[6]);
			o1.setServiceRenderedFrom((Date) e[7]);
			if(o1.getServiceRenderedFrom()!=null) {
				o1.setServiceRenderedFromFormated(simple.format(o1.getServiceRenderedFrom()));
			}
			
			o1.setServiceRenderedTo((Date) e[8]);
			if(o1.getServiceRenderedTo()!=null) {
				o1.setServiceRenderedToFormated(simple.format(o1.getServiceRenderedTo()));
			}
			
			o1.setServiceRequestBy((String) e[9]);
			o1.setCustomerCode((String) e[10]);
			o1.setCustomerName((String) e[11]);
			o1.setPoNumber((String) e[12]);
			o1.setVesselCode((String) e[13]);
			o1.setVesselName((String) e[14]);
			o1.setInVoyage((String) e[15]);
			o1.setOutVoyage((String) e[16]);
			o1.setLineCode((String) e[17]);
			o1.setCallNo((String) e[18]);
			b1=(BigDecimal) e[19];
			if(b1!=null) {
				o1.setLoa(b1);
			}
			
			o1.setLoaUom((String) e[20]);
			b1=(BigDecimal) e[21];
			if(b1!=null) {
				o1.setGt(b1);
			}
			
			o1.setAta((Date) e[22]);
			if(o1.getAta()!=null) {
				o1.setAtaFormated(simple.format(o1.getAta()));
			}
			
			o1.setAtd((Date) e[23]);
			if(o1.getAtd()!=null) {
				o1.setAtdFormated(simple.format(o1.getAtd()));
			}
			
			o1.setLocation((String) e[24]);
			o1.setRemarks((String) e[25]);
			o1.setCreditNote(cn);
			o1.setBilling(b);
			orders.add(o1);
		}
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}
//	@GetMapping("/getOneOrder/{diputeId}")
//    public ResponseEntity<?> getOneOrder(@PathVariable("diputeId") Long disputeId){
//        CreditNote  c = billingService.getOneCreditNote(disputeId);
//        Optional<Billing> b = billingService.getOne(c.getBillingId());
//        try {
//        if(b.get()!=null) {
//            Optional<OrderDetails> order = orderDetailsServiceImpl.findByBilling(b.get().getBillingId());
//order.get().setBilling(b.get());
//order.get().setCreditNote(c);
//
//        return new ResponseEntity<>(order.get(),HttpStatus.OK);
//        }
//        }catch(Exception e) {
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        //return new ResponseEntity<>(order.get(),HttpStatus.OK);
//    }
	@GetMapping("/getOneOrder/{diputeId}")
    public ResponseEntity<?> getOneOrder(@PathVariable("diputeId") Long disputeId){
        CreditNote  c = billingService.getOneCreditNote(disputeId);
        Optional<Billing> b = billingService.getOne(c.getBillingId());
        try {
        if(b.get()!=null) {
            Optional<OrderDetails> order = orderDetailsServiceImpl.findByBilling(b.get().getBillingId());
            if(order.get()!=null) {
            	List<OrderItemEntity> li = orderItemServiceImpl.findAllItemFromOrderId(order.get().getOrderId());
                List<OrderAttachment> la = orderItemServiceImpl.getAllAttachmentFromOrderId(order.get().getOrderId());
                order.get().setOrderItems(li);
                order.get().setOrderAttachments(la);
            }
            
			order.get().setCreditNote(c);
//			order.get().setCreatedBy(createdBy);
			DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			b.get().setInvoiceDateFormated(simple.format(b.get().getInvoiceDate()));
			order.get().setBilling(b.get());
			order.get().setCreatedOnFormated(simple.format(order.get().getCreatedOn()));
			order.get().setUpdatedOnFormated(simple.format(order.get().getUpdatedOn()));
			order.get().setRequestDateFormated(simple.format(order.get().getRequestDate()));
			order.get().setServiceRenderedFromFormated(simple.format(order.get().getServiceRenderedFrom()));
			order.get().setServiceRenderedToFormated(simple.format(order.get().getServiceRenderedTo()));
			order.get().setAtaFormated(simple.format(order.get().getAta()));
			order.get().setAtdFormated(simple.format(order.get().getAtd()));
			return new ResponseEntity<>(order.get(), HttpStatus.OK);
		}
        }catch(Exception e) {
        	  return new ResponseEntity<>(c,HttpStatus.OK);
        }
     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
}
	@PostMapping("/updateOneOrder")
	public ResponseEntity<?> updateOneOrder(@RequestBody OrderDetails mixofall){
	//	OrderDetails order = mixofall.getOrderDetails();
		Billing billing = mixofall.getBilling();
		CreditNote credinote = mixofall.getCreditNote();
		List<OrderItemEntity> items = mixofall.getOrderItems();
		List<OrderAttachment> attachments = mixofall.getOrderAttachments();
		if(mixofall!=null) {
			String createdBy = appUserServiceImpl.getLoggedInUser().getUsername();		mixofall.setUpdatedBy(createdBy);
			orderDetailsServiceImpl.updateOrder(mixofall);
		}
		if(billing!=null) {
			billingService.updateBilling(billing);
		}
		if(credinote!=null) {
			billingService.updateCreditNote(credinote);
		}
		if(attachments!=null) {
			attachments.forEach(o->o.setOrderId(mixofall));
			orderItemServiceImpl.updateAllAttachments(attachments);
			mixofall.setOrderAttachments(null);
		}
		if(items!=null) {
			items.forEach(o->o.setOrderId(mixofall));
			orderItemServiceImpl.updateAllItems(items);
			mixofall.setOrderItems(null);
		}
		return new ResponseEntity<>(mixofall,HttpStatus.OK);
	}
	@PostMapping("/updateOneOrdernew")
	public ResponseEntity<?> updateOneOrder(@RequestParam String o1,
			@RequestParam Map<String, MultipartFile> attachmentFile) throws Exception {
		//this method is now onward for SAVE so status =P
		OrderDetails orderDetails;
		String createdBy = appUserServiceImpl.getLoggedInUser().getUsername();
		
		orderDetails = new ObjectMapper().readValue(o1, OrderDetails.class);
		orderDetails.setUpdatedBy(createdBy);
		orderDetails.setStatus((orderDetails.getStatus().charAt(0)+"").toUpperCase());
		OrderDetails order = orderDetailsServiceImpl.addOrder(orderDetails);
		if (orderDetails.getOrderItems() != null) {
			Long i = (long) 1;
			List<OrderItemEntity> l = orderDetails.getOrderItems();
			for (OrderItemEntity item : l) {
				String s = item.getTotalUnitString().replaceAll(",", "");
				String s1 = item.getUnitPriceString().replaceAll(",", "");
				System.out.println("S = "+s+" s1 = " +s1);
				item.setTotalUnit(BigDecimal.valueOf(Double.parseDouble(s)));
				item.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(s1)));
				item.setOrderId(order);
				item.setCreatedBy(createdBy);
				item.setLineId(i);
				item.setCreatedOn(new Date());
				i++;
			}
			orderItemServiceImpl.addAllItems(l);
			l.forEach(l1 -> l1.setOrderId(null));
		}
		if (!attachmentFile.isEmpty()) {
			long c = 1;
			ArrayList<OrderAttachment> attachments = new ArrayList<OrderAttachment>();
			List<OrderAttachment> alreadySavedAttachment = this.orderItemServiceImpl.getAllAttachmentFromOrderId(orderDetails.getOrderId());
			
			
			for (Map.Entry<String, MultipartFile> e : attachmentFile.entrySet()) {
				System.out.println(e.getKey());
				OrderAttachment a = new OrderAttachment();
				a.setCreatedOn(new Date());
				a.setOrderId(order);
				a.setAttachmentId(c);
				a.setCreatedBy(createdBy);
				a.setAttachment(e.getValue().getBytes());
				a.setAttachmentType(e.getValue().getContentType());
				a.setAttachmentFilename(e.getValue().getName());
				attachments.add(a);
				c++;
			}
			orderItemServiceImpl.addAllAtthacments(attachments);
		}
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	@PostMapping("/addOneOrder")
	public ResponseEntity<?> addOneOrder(@RequestParam String o1,
			@RequestParam Map<String, MultipartFile> attachmentFile) throws Exception {
		//this method is now onward for SAVE so status =P
		OrderDetails orderDetails;
		String createdBy = appUserServiceImpl.getLoggedInUser().getUsername();
		String posisionCode = appUserServiceImpl.getLoggedInUser().getPositionCode().getPositionCode();
		String posistionDesc = appUserServiceImpl.getLoggedInUser().getPositionCode().getDescription();
		String preparedBy = createdBy;
		String departmentCode = appUserServiceImpl.getLoggedInUser().getDepartmentCode().getDepartmentCode();
		
		orderDetails = new ObjectMapper().readValue(o1, OrderDetails.class);
		orderDetails.setCreatedBy(createdBy);
		orderDetails.setUpdatedBy(createdBy);
		orderDetails.setPreparedBy(preparedBy);
		orderDetails.setDepartmentCode(departmentCode);
		orderDetails.setPreparerPositionCode(posisionCode);
		orderDetails.setPreparerPositionDesc(posistionDesc);
	//	orderDetails.setStatus("P");
		orderDetails.setStatus((orderDetails.getStatus().charAt(0)+"").toUpperCase());
		OrderDetails order = orderDetailsServiceImpl.addOrder(orderDetails);

		if (orderDetails.getOrderItems() != null) {
			Long i = (long) 1;
			List<OrderItemEntity> l = orderDetails.getOrderItems();

			for (OrderItemEntity item : l) {
				String s = item.getTotalUnitString().replaceAll(",", "");
				String s1 = item.getUnitPriceString().replaceAll(",", "");
				System.out.println("S = "+s+" s1 = " +s1);
				item.setTotalUnit(BigDecimal.valueOf(Double.parseDouble(s)));
				item.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(s1)));
				item.setOrderId(order);
				item.setCreatedBy(createdBy);
				item.setLineId(i);
				item.setCreatedOn(new Date());
				i++;
			}
			orderItemServiceImpl.addAllItems(l);
			l.forEach(l1 -> l1.setOrderId(null));

		}
		if (!attachmentFile.isEmpty()) {
			long c = 1;
			ArrayList<OrderAttachment> attachments = new ArrayList<OrderAttachment>();
			for (Map.Entry<String, MultipartFile> e : attachmentFile.entrySet()) {
				System.out.println(e.getKey());
				OrderAttachment a = new OrderAttachment();
				a.setOrderId(order);
				a.setAttachmentId(c);
				a.setCreatedBy(createdBy);
				a.setAttachment(e.getValue().getBytes());
				a.setAttachmentType(e.getValue().getContentType());
				a.setAttachmentFilename(e.getValue().getName());
				attachments.add(a);
				c++;
			}
			orderItemServiceImpl.addAllAtthacments(attachments);
		}
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	@PostMapping("/readyForApproval")
	public ResponseEntity<?> readyForApprovalNow(@RequestBody OrderDetails orderDetails){
		//this method is when ready for approval is done
		Optional<OrderDetails> o = orderDetailsServiceImpl.getOrderById(orderDetails.getOrderId());
		if(o.get()!=null) {
			String updatedBy = appUserServiceImpl.getLoggedInUser().getUsername();
			o.get().setStatus("D");
			o.get().setUpdatedBy(updatedBy);
			orderDetailsServiceImpl.updateOrder(o.get());
		}
		return new ResponseEntity<>(o.get(),HttpStatus.OK);
	}
//	@PostMapping("/addOneOrder")
//	public ResponseEntity<OrderDetails>  addOneOrder(@RequestBody OrderDetails orderDetails){
//		OrderDetails order = orderDetailsServiceImpl.addOrder(orderDetails);
//		
//		if(orderDetails.getOrderItems()!=null) {
//			orderDetails.getOrderItems().forEach(orderItemEntity->orderItemEntity.setOrderId(order));
//		}
//		return  new ResponseEntity<>(order,HttpStatus.OK);
//		
//		//		if(mixOfAll.getCrediteNote()!=null) {
////			CreditNote creditNote= billingService.addCreditNote(mixOfAll.getCrediteNote());
////			if(mixOfAll.getBilling()!=null) {
////				mixOfAll.getBilling().setBillingId(creditNote.getBillingId());
////				Billing billing = billingService.addBilling(mixOfAll.getBilling());
////			}
////		}
////		if(mixOfAll.getOrderDetails()!=null) {
////			mixOfAll.getOrderDetails().setCreditNote(mixOfAll.getCrediteNote());
////			OrderDetails order = orderDetailsServiceImpl.addOrder(mixOfAll.getOrderDetails());	
////			if(mixOfAll.getOrderAttachment()!=null) {
////				if(mixOfAll.getOrderItemEntity().size()!=0) {
////					mixOfAll.getOrderItemEntity().forEach(orderItemEntity ->orderItemEntity.setOrderId(order));
////					List<OrderItemEntity> items = orderItemServiceImpl.addAllItems(mixOfAll.getOrderItemEntity());
////				}
////			}
////			if(mixOfAll.getOrderAttachment()!=null) {
////				if(mixOfAll.getOrderAttachment().size()!=0) {
////					mixOfAll.getOrderAttachment().forEach(orderAttachment -> orderAttachment.setOrderId(order));
////					List<OrderAttachment> attachments = orderItemServiceImpl.addAllAtthacments(mixOfAll.getOrderAttachment());
////				}
////			}
//	//	}
//		//return new ResponseEntity<>(mixOfAll,HttpStatus.OK);
//	}
	@DeleteMapping("/deleteOneOrder/{disputeId}")
	public ResponseEntity<?> deleteOneOrder(@PathVariable Long disputeId){
		CreditNote creditNote= billingService.getOneCreditNote(disputeId);
	//	billingService.deleteBilling(creditNote.getBillingId());
		Optional<OrderDetails> order = orderDetailsServiceImpl.findByBilling(creditNote.getBillingId());
		if(order.get()!=null) {
			List<OrderItemEntity> items = orderItemServiceImpl.findAllItemFromOrderId(order.get().getOrderId());
			List<OrderAttachment> attachments = orderItemServiceImpl.getAllAttachmentFromOrderId(order.get().getOrderId());
			for(OrderItemEntity item: items) {
				orderItemServiceImpl.deleteItem(item.getOrderId().getOrderId());
			}
			for(OrderAttachment attachment: attachments) {
				orderItemServiceImpl.deleteAttachment(attachment.getOrderId().getOrderId());
			}
			orderDetailsServiceImpl.deleteOrder(order.get().getOrderId());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/getPreparedBy")
	public ResponseEntity<?> getPreparedBy(){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		String prparedBy = appUserServiceImpl.getLoggedInUser().getUsername();
		String psitionDesc = appUserServiceImpl.getLoggedInUser().getPositionCode().getDescription();
		String rquestDate = simple.format(new Date());
		Object o = new Object() {
			private String preparedBy = prparedBy;
			private String positionDesc = psitionDesc;
			private String requestDate = rquestDate;
			
			public String getPreparedBy() {
				return preparedBy;
			}
			public void setPreparedBy(String preparedBy) {
				this.preparedBy = preparedBy;
			}
			public String getPositionDesc() {
				return positionDesc;
			}
			public void setPositionDesc(String positionDesc) {
				this.positionDesc = positionDesc;
			}
			public String getRequestDate() {
				return requestDate;
			}
			public void setRequestDate(String requestDate) {
				this.requestDate = requestDate;
			}
			
		};
		return new ResponseEntity<>(o,HttpStatus.OK);
	}
//	@GetMapping("/test/{page}")
//	public ResponseEntity<?> getIt(
//			@PathVariable int page,
//			@RequestParam(value = "size", defaultValue = "10", required = false) String size){
//		Pageable paging = PageRequest.of(page, 10);
////		if(size!=null || !size.equalsIgnoreCase("undefined")) {
////			if(page!=null || !page.equalsIgnoreCase("undefined")) {
////				paging = PageRequest.of(Integer.valueOf(page), Integer.valueOf(size));
////			}else {
////				 paging = PageRequest.of(0, Integer.valueOf(size));
////			}
////		}
//		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//
//		Page<Object> p = this.orderDetailsServiceImpl.readAndCollect(paging);
//		List<Object> l  = p.getContent();
//		List<OrderDetails> l1 = new ArrayList<>();
//		for(Object o:l) {
//			OrderDetails o1 = new OrderDetails();
//			Object[] e = (Object[]) o;
//			o1.setRequestDate((Date) e[0]);
//			o1.setRequestDateFormated(simple.format(o1.getRequestDate()));
//			o1.setDescription((String) e[1]);
//			o1.setCustomerCode((String) e[2]);
//			o1.setCustomerName((String) e[3]);
//			o1.setVesselCode((String) e[4]);
//			o1.setVesselName((String) e[5]);
//			o1.setInVoyage((String) e[6]);
//			o1.setOutVoyage((String) e[7]);
//			o1.setPreparedBy((String) e[8]);
//			String s = appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",(String) e[9]);
//			BigDecimal t = (BigDecimal) e[10];
//			o1.setOrderId(t.longValue());
//			o1.setInvoiceType((String) e[11]);
//			o1.setStatus(s);
//			l1.add(o1);
//		}
//		Object obj = new Object() {
//			private List<OrderDetails> content = l1;
//			private int totalPages = p.getTotalPages();
//			private int currentPage = p.getNumber();
//			private long totalElements = p.getTotalElements();
//			private int pageSize = p.getSize();
//			public List<OrderDetails> getContent() {
//				return content;
//			}
//			public void setContent(List<OrderDetails> content) {
//				this.content = content;
//			}
//			public int getTotalPages() {
//				return totalPages;
//			}
//			public void setTotalPages(int totalPages) {
//				this.totalPages = totalPages;
//			}
//			public int getCurrentPage() {
//				return currentPage;
//			}
//			public void setCurrentPage(int currentPage) {
//				this.currentPage = currentPage;
//			}
//			
//			public long getTotalElements() {
//				return totalElements;
//			}
//			public void setTotalElements(long totalElements) {
//				this.totalElements = totalElements;
//			}
//			public int getPageSize() {
//				return pageSize;
//			}
//			public void setPageSize(int pageSize) {
//				this.pageSize = pageSize;
//			}
//		};
//		System.out.println(l1.size());
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
	
	@GetMapping("/test2")
	public ResponseEntity<?> getIt2() throws JsonMappingException, JsonProcessingException{


		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//		Collection<String> cacheNames = cacheManager.getCacheNames();
//		for(String c: cacheNames) {
//			System.out.println(c);
//			Cache cache = cacheManager.getCache(c);
//			Page<Object> p = cache.get(paging, Page.class);
//		}
		

		List<Object> l  = this.orderDetailsServiceImpl.readAndCollect();
 
		List<OrderDetails> l1 = new ArrayList<>();
		for(Object o:l) {
			OrderDetails o1 = new OrderDetails();
			Object[] e = (Object[]) o;
			o1.setRequestDate((Date) e[0]);
			o1.setRequestDateFormated(simple.format(o1.getRequestDate()));
			o1.setDescription((String) e[1]);
			o1.setCustomerCode((String) e[2]);
			o1.setCustomerName((String) e[3]);
			o1.setVesselCode((String) e[4]);
			o1.setVesselName((String) e[5]);
			o1.setInVoyage((String) e[6]);
			o1.setOutVoyage((String) e[7]);
			o1.setPreparedBy((String) e[8]);
			String st = (String) e[9];
			String s=null;
			if(st!=null) {
				if(st.equals("P")) {
					s="Pending";
				}else if(st.equals("R")){
					s="Rejected";
				}else if(st.equals("D")) {
					s="Ready for Approval";
				}else if(st.equals("C")) {
					s="Cancelled";
				}else {
					s="Approved";
				}
			}else {
				s=null;
			}
			BigDecimal t = (BigDecimal) e[10];
			o1.setOrderId(t.longValue());
			o1.setInvoiceType((String) e[11]);
			o1.setStatus(s);
			l1.add(o1);
		}
		Object obj = new Object() {
			private List<OrderDetails> content = l1;

			public List<OrderDetails> getContent() {
				return content;
			}
			public void setContent(List<OrderDetails> content) {
				this.content = content;
			}

		};
		System.out.println(l1.size());
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	@GetMapping("/test3")
	public ResponseEntity<?> getIt3() throws JsonMappingException, JsonProcessingException{
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	
		List<Object> l  = orderDetailsServiceImpl.readAndCollect1();
		List<OrderDetails> l1 = new ArrayList<>();
		for(Object o:l) {
			OrderDetails o1 = new OrderDetails();
			Object[] e = (Object[]) o;
			o1.setRequestDate((Date) e[0]);
			o1.setRequestDateFormated(simple.format(o1.getRequestDate()));
			o1.setDescription((String) e[1]);
			o1.setCustomerCode((String) e[2]);
			o1.setCustomerName((String) e[3]);
			o1.setVesselCode((String) e[4]);
			o1.setVesselName((String) e[5]);
			o1.setInVoyage((String) e[6]);
			o1.setOutVoyage((String) e[7]);
			o1.setPreparedBy((String) e[8]);
			String st = (String) e[9];
			String s=null;
			if(st!=null) {
				if(st.equals("P")) {
					s="Pending";
				}else if(st.equals("R")){
					s="Rejected";
				}else if(st.equals("D")) {
					s="Ready for Approval";
				}else if(st.equals("C")) {
					s="Cancelled";
				}else {
					s="Approved";
				}
			}else {
				s=null;
			}
			BigDecimal t = (BigDecimal) e[10];
			o1.setOrderId(t.longValue());
			o1.setInvoiceType((String) e[11]);
			o1.setStatus(s);
			l1.add(o1);
		}
		Object obj = new Object() {
			private List<OrderDetails> content = l1;

			public List<OrderDetails> getContent() {
				return content;
			}
			public void setContent(List<OrderDetails> content) {
				this.content = content;
			}
		};
		System.out.println(l1.size());
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
}