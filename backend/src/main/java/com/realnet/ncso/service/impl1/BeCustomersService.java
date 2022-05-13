package com.realnet.ncso.service.impl1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.BeCustomers;
import com.realnet.ncso.repository1.BeCustomerRepository;

@Service
public class BeCustomersService {
	private BeCustomerRepository beCustomerRepository;
	@Autowired
	public BeCustomersService(BeCustomerRepository beCustomerRepository) {
		super();
		this.beCustomerRepository = beCustomerRepository;
	}
	
	public BeCustomers addOne(BeCustomers beCustomers) {
		return beCustomerRepository.save(beCustomers);
	}
	public List<BeCustomers> getAll(Pageable pageable){
		return beCustomerRepository.findAll(pageable).getContent();
	}
	public Optional<BeCustomers> getOne(Long customerId){
		return beCustomerRepository.findById(customerId);
	}
	public BeCustomers updateOne(BeCustomers becustomers) {
		return beCustomerRepository.save(becustomers);
	}
	public void deleteOne(Long id) {
		beCustomerRepository.deleteById(id);
	}
}
