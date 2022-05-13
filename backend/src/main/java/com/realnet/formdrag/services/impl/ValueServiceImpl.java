//package com.realnet.formdrag.services.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.realnet.formdrag.entity.Attributes;
//import com.realnet.formdrag.entity.Value;
//import com.realnet.formdrag.repository.ValueRepository;
//import com.realnet.formdrag.services.ValueService;
//
//@Service
//public class ValueServiceImpl implements ValueService {
//	
//	@Autowired
//	private ValueRepository valueRepository;
//
//	@Override
//	public Value createValue(Value value) {
//		return this.valueRepository.save(value);
//	}
//
//	@Override
//	public Value updateValue(Value value) {
//		return this.valueRepository.save(value);
//	}
//
//	@Override
//	public Value getValue(Long vId) {
//		return this.valueRepository.findById(vId).get();
//	}
//
//	@Override
//	public List<Value> getAllValues() {
//		return this.valueRepository.findAll();
//	}
//
//	@Override
//	public void deleteValue(Long vId) {
//		this.valueRepository.deleteById(vId);
//	}
//
////	@Override
////	public List<Value> getValueByAttribute(Attributes attributes) {
////		return this.valueRepository.findByAttribute(attributes);
////	}
//
//}
