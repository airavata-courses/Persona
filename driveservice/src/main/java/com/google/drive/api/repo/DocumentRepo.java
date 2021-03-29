package com.google.drive.api.repo;

import com.google.drive.api.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {

}
