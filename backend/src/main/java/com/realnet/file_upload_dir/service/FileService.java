package com.realnet.file_upload_dir.service;

import java.util.List;

import com.realnet.file_upload_dir.entity.FileDetails;

public interface FileService {
	
	public FileDetails addToDb(FileDetails fileDetails);
	
	public FileDetails updateToDb(FileDetails fileDetails);
	
	public FileDetails getOneById(Long id);
	
	public List<FileDetails> getAllFromDb();
	
	public void deleteFromDbById(Long id);
}
