package com.realnet.formio.Services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.formio.Services.CardService;
import com.realnet.formio.entity.Card;
import com.realnet.formio.entity.Column;
import com.realnet.formio.repository.CardRepository;

@Service
public class CardServiceImpl implements CardService {
	
	@Autowired
	private CardRepository cardRepository;

	@Override
	public Card createCard(Card card) {
		return this.cardRepository.save(card);
	}

	@Override
	public Card updateCard(Card card) {
		return this.cardRepository.save(card);
	}

	@Override
	public Card getCard(Long id) {
		return this.cardRepository.findById(id).get();
	}

	@Override
	public List<Card> getAllCards() {
		return this.cardRepository.findAll();
	}

	@Override
	public void deleteCard(Long id) {
		this.cardRepository.deleteById(id);
	}

	@Override
	public List<Card> getCardsOfColumn(Column column) {
		
		return this.cardRepository.getCardsByColumn(column);
	}

}
