package com.realnet.ncso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.ncso.entity.Attachments;

public interface AttachmentRepository extends JpaRepository<Attachments, Long>{
//	Optional<Attachments> findByName(String name);
}
