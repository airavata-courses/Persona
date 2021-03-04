package com.session.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.Gson;
import com.session.service.SessionService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SessionController {
	
	@Autowired
	SessionService sessionService;

	@PostMapping("/session")
	public ResponseEntity saveSession(@RequestBody String body) {
		String username=(String) new Gson().fromJson(body, Map.class).get("username");
		if(username==null)
			return ResponseEntity.badRequest().build();
		String sessionId= sessionService.createSession(username);
		return new ResponseEntity(sessionId,HttpStatus.CREATED);
		

	}
	@PostMapping("/validate")
	public boolean validateSession(@RequestBody String body) {
		String sessionId=(String) new Gson().fromJson(body, Map.class).get("sessionId");
		if(sessionId!=null) {
			return sessionService.validateSession(sessionId);
		}
		return false;
	}
	@PostMapping("/invalidate")
	public String invalidateSession(@RequestBody String body) {
		String sessionId=(String) new Gson().fromJson(body, Map.class).get("sessionId");
		if(sessionId!=null) {
			return sessionService.invalidateSession(sessionId);
		}
		return "Please login again";
	}

}
