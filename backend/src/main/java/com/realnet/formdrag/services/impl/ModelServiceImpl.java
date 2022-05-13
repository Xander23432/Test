//package com.realnet.formdrag.services.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.realnet.formdrag.entity.ModelEntity;
//import com.realnet.formdrag.repository.ModelRepository;
//import com.realnet.formdrag.services.ModelService;
//
//@Service
//public class ModelServiceImpl implements ModelService {
//	
//	@Autowired
//	private ModelRepository modelRepository;
//
//	@Override
//	public ModelEntity createModel(ModelEntity modelEntity) {
//		return this.modelRepository.save(modelEntity);
//	}
//
//	@Override
//	public ModelEntity updateModel(ModelEntity modelEntity) {
//		return this.modelRepository.save(modelEntity);
//	}
//
//	@Override
//	public ModelEntity getModel(Long mId) {
//		return this.modelRepository.findById(mId).get();
//	}
//
//	@Override
//	public List<ModelEntity> getAllModel() {
//		return this.modelRepository.findAll();
//	}
//
//	@Override
//	public void deleteModel(Long mId) {
//		this.modelRepository.deleteById(mId);
//	}
//
//}
