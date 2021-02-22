package com.file.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.file.response.DocumentResponse;

public interface FileService {

	
	public String upload(MultipartFile[] files, String userName) throws Exception;
	
	public ByteArrayOutputStream download(List<Long> ids, String userName) throws Exception;
	
	public Set<DocumentResponse> getFilesByUsername(String userName);
	
	
}
