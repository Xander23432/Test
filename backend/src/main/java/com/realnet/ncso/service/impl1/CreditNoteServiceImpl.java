package com.realnet.ncso.service.impl1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity1.CreditNote;
import com.realnet.ncso.repository1.CreditNoteRepository;

@Service
public class CreditNoteServiceImpl {
	private CreditNoteRepository creditNoteRepository;
	@Autowired
	public CreditNoteServiceImpl(CreditNoteRepository creditNoteRepository) {
		super();
		this.creditNoteRepository = creditNoteRepository;
	}
	public List<CreditNote> getAll(Pageable page) {
		return creditNoteRepository.findAll(page).toList();
	}
	
}
