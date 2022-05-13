package com.realnet.file_upload_dir.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.file_upload_dir.entity.FileDetails;
import com.realnet.file_upload_dir.repository.FileRepository;
import com.realnet.file_upload_dir.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	private FileRepository fileRepository;
	

	@Override
	public FileDetails addToDb(FileDetails fileDetails) {
		FileDetails save = this.fileRepository.save(fileDetails);
		return save;
	}

	@Override
	public FileDetails updateToDb(FileDetails fileDetails) {
		FileDetails save = this.fileRepository.save(fileDetails);
		return save;
	}

	@Override
	public FileDetails getOneById(Long id) {
		Optional<FileDetails> findById = this.fileRepository.findById(id);
		return findById.get();
	}

	@Override
	public List<FileDetails> getAllFromDb() {
		List<FileDetails> findAll = this.fileRepository.findAll();
		return findAll;
	}

	@Override
	public void deleteFromDbById(Long id) {
		this.fileRepository.deleteById(id);
		System.out.println("Deleted success...");
	}

}
