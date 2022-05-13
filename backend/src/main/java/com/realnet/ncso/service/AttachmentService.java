package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.Attachments;

public interface AttachmentService {
	
	public Attachments addToDb(Attachments attachments);
	
	public Attachments updateToDb(Attachments attachments);
	
	public Attachments getOneAttchById(Long id);
	
	public List<Attachments> getAllAttach();
	
	public void deleteAttchById(Long id);

}
