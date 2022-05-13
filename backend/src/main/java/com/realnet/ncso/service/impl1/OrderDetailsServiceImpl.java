package com.realnet.ncso.service.impl1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realnet.ncso.entity1.OrderDetails;
import com.realnet.ncso.repository1.EbsInvoiceTypeRepository;
import com.realnet.ncso.repository1.OrderDetailsRepository;
import com.realnet.ncso.service1.OrderDetailsService;
@Service
public class OrderDetailsServiceImpl  implements OrderDetailsService{
	
	private OrderDetailsRepository orderDetailsRepository;
	private EbsInvoiceTypeRepository ebsInvoiceTypeRepository;
	private EntityManager entityManager;
	@Autowired
	public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository,
			EbsInvoiceTypeRepository ebsInvoiceTypeRepository,
			EntityManager entityManager) {
		super();
		this.orderDetailsRepository = orderDetailsRepository;
		this.ebsInvoiceTypeRepository = ebsInvoiceTypeRepository;
		this.entityManager=entityManager;
	}
	
	public List<OrderDetails> getAllByDeptAndByGrpLevel(Pageable page,Long groupLevel,String departmentCode){
		List<OrderDetails> a= orderDetailsRepository.getAllByDeptByGrpLvl(page,groupLevel,departmentCode).getContent();
		return a;
	}
	
	@Override
	public OrderDetails addOrder(OrderDetails orderDetails) {
//		String inParam1 = "NC_SERVICE_ORDER";
//		Date inParam2 = new Date();
//		String inParam3=null;
//		String outParam1 = orderDetailsRepository.inOnlyTest(inParam1,inParam2,inParam3);
//		Long orderId = Long.parseLong(outParam1);
//		orderDetails.setOrderId(orderId);
		//System.out.println(orderDetails);
		OrderDetails o =  orderDetailsRepository.save(orderDetails);
		return o;
	}
	@Override
	public OrderDetails updateOrder(OrderDetails orderDetails) {
		return orderDetailsRepository.save(orderDetails);
	}
	@Override
	public Optional<OrderDetails> getOrderById(Long id){
		return  orderDetailsRepository.findById(id);
	}
	@Override
	public List<OrderDetails> getAllOrders(){
		return orderDetailsRepository.findAll();
	}
	@Override
	public void deleteOrder(Long id) {
		orderDetailsRepository.deleteById(id);
	}
	
	public Optional<OrderDetails> findByBilling(Long billingId){
		return orderDetailsRepository.findByBillingId(billingId);
	}
	
	public List<OrderDetails> findByCreditNote(){
		return orderDetailsRepository.findByCreditNoteNo();
	}
	public List<OrderDetails> findAll(Pageable pageable){
		
		Page<OrderDetails> orders = orderDetailsRepository.findAll(pageable);
		List<OrderDetails> o = orders.getContent();
		//o.forEach(order->order.setCustomerName(orderDetailsRepository.findCustomerName(order.getCustomerCode()).get()));
		for(OrderDetails order:o) {
			if(order.getCustomerCode()!=null) {
				String s = orderDetailsRepository.findCustomerName(order.getCustomerCode());
				try {
					if(s!=null) {
						order.setCustomerName(s);
					}
				}catch(Exception e) {
					
				}
			}
		}
		return o;
	}
	@Cacheable(cacheNames = {"user","user"})
	public List<Object> getAllCustomCreditNote(String str){
		return orderDetailsRepository.getAllCreditCustom(str);
	}
	public List findAll1(Pageable pageable){
		Page<OrderDetails> orders = orderDetailsRepository.findByCreditNoteNotNull(pageable);
		List<OrderDetails> o = orders.getContent();
		//o.forEach(order->order.setCustomerName(orderDetailsRepository.findCustomerName(order.getCustomerCode()).get()));
		for(OrderDetails order:o) {
			if(order.getCustomerCode()!=null) {
				String s = orderDetailsRepository.findCustomerName(order.getCustomerCode());
				try {
					if(s!=null) {
						order.setCustomerName(s);
					}
				}catch(Exception e) {
					
				}
			}
		}
		List<Object> l = new ArrayList<>();
		l.add(orders);
		l.add(o);
		return l;
	}
	public String findCustomerName(String customerCode) {
		String s = orderDetailsRepository.findCustomerName(customerCode);
		return s;
	}

	public List<OrderDetails> getAllOrderOfCurrentUser(Pageable pageable,String username) {
		// TODO Auto-generated method stub
		return orderDetailsRepository.findByPreparedBy(pageable,username);
	}
	@Cacheable(cacheNames = {"user","user"})
	public List<Object> readAndCollect() {
		List<Object> o = orderDetailsRepository.findAllCustom();
		return o;
	}
	@Transactional(readOnly = true)
	public List<Object> readAndCollect1(){
		Stream<Object> objs = orderDetailsRepository.findAllCustom1();
		List<Object> l=new ArrayList<>();
		objs.forEach(o->{
			l.add(o);
		});
		return l;
	}

	public List<Object> getAllCreditNoteWithoutPagination() {
		// TODO Auto-generated method stub
		return orderDetailsRepository.getAllCreditCustomWithoutPagination();
	}
}
