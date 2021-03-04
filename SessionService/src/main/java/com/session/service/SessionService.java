package com.session.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.session.entity.SessionEntity;
import com.session.repository.ISessionRepository;

@Service
public class SessionService {
	
	private final int timeout=1800000;
	
	@Autowired
	private ISessionRepository sessionRepository;
	
	public String createSession(String username) {
		SessionEntity session= sessionRepository.findByUsername(username);
		Date date=new Date();
		System.out.println(session);
		if(session!=null&&date.getTime()<=(session.getLastModified().getTime()+session.getTimeout())) {
			return session.getSessionId();
		}
		SessionEntity entity=new SessionEntity();
		String sessionId=UUID.randomUUID().toString();
		Date curdate=new Date();
		entity.setUsername(username);
		entity.setSessionId(sessionId);
		entity.setStartTime(curdate);
		entity.setLastModified(curdate);
		entity.setTimeout(timeout);
		
		sessionRepository.save(entity);
		return sessionId;
	}
	public boolean validateSession(String sessionId) {
		SessionEntity session= sessionRepository.findBySessionId(sessionId);
		Date date=new Date();
		if(session==null||date.getTime()>(session.getLastModified().getTime()+session.getTimeout())) {
			return false;
		}
		session.setLastModified(new Date());
		sessionRepository.save(session);
		return true;
	}
	
	public String invalidateSession(String sessionId){
		SessionEntity entity=sessionRepository.findBySessionId(sessionId);
		if(entity!=null) {
			entity.setTimeout(0);
			sessionRepository.save(entity);
		}
		
		return "Succesfully logged out";
	}

}
