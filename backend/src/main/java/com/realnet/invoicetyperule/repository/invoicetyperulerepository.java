package com.realnet.invoicetyperule.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.realnet.invoicetyperule.entity.invoicetyperuleentity;

@Repository
public interface invoicetyperulerepository extends JpaRepository<invoicetyperuleentity, Integer>{
	Page<invoicetyperuleentity> findAll(Pageable p);
}
