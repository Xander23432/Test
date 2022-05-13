package com.realnet.fnd.service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.comm.entity.ProductEntity;
public interface ProductService {
	 public Page<ProductEntity> getlist(Pageable page);
	 
	  public List<ProductEntity> saveproduct(List<ProductEntity> product);
	    public ProductEntity getid(int id);
	    public ProductEntity updateById(int id, ProductEntity productEntity);
	    public boolean deleteById(int id);
}
