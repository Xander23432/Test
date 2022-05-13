package com.realnet.formio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.realnet.formio.entity.CardImage;

public interface ImageRepository extends JpaRepository<CardImage, Long> {
	Optional<CardImage> findByName(String name);
}
