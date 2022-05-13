package com.realnet.ncso.service;

import java.util.List;

import com.realnet.ncso.entity.Items;

public interface ItemService {
	
public Items addToDb(Items attachments);
	
	public Items updateToDb(Items attachments);
	
	public Items getOneAttchById(Long id);
	
	public List<Items> getAllAttach();
	
	public void deleteAttchById(Long id);

}
