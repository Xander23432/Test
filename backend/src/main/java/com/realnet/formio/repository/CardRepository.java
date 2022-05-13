package com.realnet.formio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.formio.entity.Card;
import com.realnet.formio.entity.Column;

public interface CardRepository extends JpaRepository<Card, Long> {
	
	public List<Card> getCardsByColumn(Column column);

}
