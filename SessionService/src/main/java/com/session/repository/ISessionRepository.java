package com.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.session.entity.SessionEntity;

public interface ISessionRepository extends JpaRepository<SessionEntity, String>{

	@Query(value = "select * from session where session_id=?1 limit 1",nativeQuery = true)
	public SessionEntity findBySessionId(String SessionId);
	
	@Query(value = "select * from session where username=?1 order by last_modified desc limit 1",nativeQuery = true)
	public SessionEntity findByUsername(String username);
}
