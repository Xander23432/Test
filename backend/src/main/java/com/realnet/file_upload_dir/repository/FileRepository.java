package com.realnet.file_upload_dir.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.file_upload_dir.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails, Long>{
	
	

}
