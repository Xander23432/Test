package com.realnet.invoicetyperule.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.department.entity.Departmententity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.invoicetyperule.entity.invoicetyperuleentity;
import com.realnet.invoicetyperule.repository.invoicetyperulerepository;
@Service
public class invoicetyperuleserviceimpl implements invoicetyperuleservice{

	@Autowired
	private	invoicetyperulerepository  invoicerepository;
	public Page<invoicetyperuleentity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return invoicerepository.findAll(page);
	}
	

	@Override
	public invoicetyperuleentity createCollageStudent(invoicetyperuleentity data) {
		// TODO Auto-generated method stub
		return invoicerepository.save(data);
	}

	@Override
	public invoicetyperuleentity getid(int id) {
		// TODO Auto-generated method stub
		invoicetyperuleentity data = this.invoicerepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rn_Instructor not found :: " + id));
		return data;
	}

	@Override
	public invoicetyperuleentity updateById(int id, invoicetyperuleentity invoiceentity) {
		// TODO Auto-generated method stub
		invoicetyperuleentity data = invoicerepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(" not found :: " + id));
			
		data.setINVOICE_TYPE(invoiceentity.getINVOICE_TYPE());
		
		
		data.setREQUIRE_PAIRING_EVENT(invoiceentity.getREQUIRE_PAIRING_EVENT());
		
		
		data.setREQUIRE_SCHEDULE(invoiceentity.getREQUIRE_SCHEDULE());
		
		
		data.setAPPLY_GROUP_ID(invoiceentity.getAPPLY_GROUP_ID());
		
		
		data.setUOM(invoiceentity.getUOM());

        data.setFIXED_VALUE(invoiceentity.getFIXED_VALUE());
		
		
		data.setDESCRIPTION(invoiceentity.getDESCRIPTION());
		data.setACTIVE(invoiceentity.getACTIVE());
		data.setREQUIRE_CONFIRM(invoiceentity.getREQUIRE_CONFIRM());
		data.setCREATED_ON(invoiceentity.getCREATED_ON());
		data.setCREATED_BY(invoiceentity.getCREATED_BY());
		data.setUPDATED_ON(invoiceentity.getUPDATED_ON());
		data.setUPDATED_BY(invoiceentity.getUPDATED_BY());
		
		invoicerepository.save(data);
		
		return data;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
if (!invoicerepository.existsById(id)) {
			
			throw new ResourceNotFoundException(" not found :: " + id);
		}
		
       invoicetyperuleentity data = invoicerepository.findById(id).orElse(null);
          invoicerepository.delete(data);
		return true;
	}
	

}
