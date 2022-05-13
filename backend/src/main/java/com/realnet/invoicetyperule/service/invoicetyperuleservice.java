package com.realnet.invoicetyperule.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.invoicetyperule.entity.invoicetyperuleentity;

public interface invoicetyperuleservice {
	public Page<invoicetyperuleentity> getlist(Pageable page);
    public invoicetyperuleentity createCollageStudent(invoicetyperuleentity data);
    public invoicetyperuleentity getid(int id);
    public invoicetyperuleentity updateById(int id, invoicetyperuleentity invoiceentity);
    public boolean deleteById(int id);
}
