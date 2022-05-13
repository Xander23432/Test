package com.realnet.ncso.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.ncso.entity.Attachments;
import com.realnet.ncso.repository.AttachmentRepository;
import com.realnet.ncso.service.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService {
	
	@Autowired
	private AttachmentRepository attachmentRepository;

	@Override
	public Attachments addToDb(Attachments attachments) {
		Attachments save = this.attachmentRepository.save(attachments);
		return save;
	}

	@Override
	public Attachments updateToDb(Attachments attachments) {
		Attachments save = this.attachmentRepository.save(attachments);
		return save;
	}

	@Override
	public Attachments getOneAttchById(Long id) {
		Optional<Attachments> findById = this.attachmentRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<Attachments> getAllAttach() {
		List<Attachments> findAll = this.attachmentRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteAttchById(Long id) {
		this.attachmentRepository.deleteById(id);
		System.out.println("Deletion successfully...");
	}

}
