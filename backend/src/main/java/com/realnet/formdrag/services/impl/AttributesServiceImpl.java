//package com.realnet.formdrag.services.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.realnet.formdrag.entity.Attributes;
//import com.realnet.formdrag.entity.ModelEntity;
//import com.realnet.formdrag.repository.AttributesRepository;
//import com.realnet.formdrag.services.AttributesService;
//
//@Service
//public class AttributesServiceImpl implements AttributesService {
//	
//	@Autowired
//	private AttributesRepository attributesRepository;
//
//	@Override
//	public Attributes createAttributes(Attributes attributes) {
//		return this.attributesRepository.save(attributes);
//	}
//
//	@Override
//	public Attributes updateAttributes(Attributes attributes) {
//		return this.attributesRepository.save(attributes);
//	}
//
//	@Override
//	public Attributes getAttribute(Long aId) {
//		return this.attributesRepository.findById(aId).get();
//	}
//
//	@Override
//	public List<Attributes> getAllAttributes() {
//		return this.attributesRepository.findAll();
//	}
//
//	@Override
//	public void deleteAttribute(Long aId) {
//		this.attributesRepository.deleteById(aId);
//	}
//
////	@Override
////	public List<Attributes> getAttributesByModel(ModelEntity model) {
////		return this.attributesRepository.findByModel(model);
////	}
//
////	@Override
////	public List<Attributes> addAll(List<Attributes> attribute) {
//////		this.attributesRepository.saveAll(Arrays.asList(attribute));
////		return null;
////	}
//
//}
