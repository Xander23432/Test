package com.realnet.comm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.comm.entity.Order;


public interface OrderRepo extends JpaRepository<Order, Integer> {
    public List<Order> findAll();
    public Page<Order> findAll(Pageable p);

}

