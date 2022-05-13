package com.realnet.fnd.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.comm.entity.CollegeStudentEntity;
import com.realnet.comm.entity.ProductEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	private	ProductRepository  productRepository;
	public Page<ProductEntity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return productRepository.findAll(page);
	}
	
	@Override
	public ProductEntity updateById(int id, ProductEntity productEntity) {
		
		ProductEntity product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("product not found :: " + id));
			
		product.setProductname(productEntity.getProductname());
		
		
		product.setQuantity(productEntity.getQuantity());
		
		
		product.setDate(productEntity.getDate());
		
		
		product.setPhone(productEntity.getPhone());
		
		
		product.setPrice(productEntity.getPrice());
		
		productRepository.save(product);
		
		return product;
	}
	
	
	
	public boolean deleteById(int id) {
		if (!productRepository.existsById(id)) {
			
			throw new ResourceNotFoundException("Product  not found :: " + id);
		}
		
		ProductEntity product = productRepository.findById(id).orElse(null);
		productRepository.delete(product);
		return true;
	}
	@Override
	public ProductEntity getid(int id) {
		
		ProductEntity orElseThrow = this.productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product not found :: " + id));
		return orElseThrow;
	}

	@Override
	public List<ProductEntity> saveproduct(List<ProductEntity> product) {
		// TODO Auto-generated method stub
			List<ProductEntity> saveAll = this.productRepository.saveAll(product);
		
		return saveAll;
	}
	
}
