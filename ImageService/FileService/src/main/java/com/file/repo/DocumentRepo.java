package com.file.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.entity.Document;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
	
	public Set<Document> findByUserName(String username);

}
