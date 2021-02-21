package com.file.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.response.DocumentResponse;
import com.file.service.FileService;

@RestController
@RequestMapping("file")
public class FileController {

	@Autowired
	private FileService fileService;

	@GetMapping
	public String get() {
		return "SUCCESS";
	}

	@PostMapping("upload/{username}")
	public ResponseEntity<String> uploadFile(
			@RequestPart("files") MultipartFile[] files,
			@PathVariable("username") String userName) throws Exception {
		System.out.println("file upload");
		String response = fileService.upload(files, userName);
		return new ResponseEntity<String>(response, HttpStatus.OK);

	}
	
	@PostMapping("download")
	public void uploadFile(
			@RequestBody List<Long> ids,
			@RequestParam("username") String userName,HttpServletResponse response) throws Exception {
		
		ByteArrayOutputStream outputStream = fileService.download(ids,userName);
		response.getOutputStream().write(outputStream.toByteArray());
		response.setContentType("application/zip");
		response.setHeader("Content-disposition", "attachment; filename=sample.zip");
	}
	
	@GetMapping("getFiles")
	public Set<DocumentResponse> getFilesByUsername(@RequestParam("username") String userName){
		return fileService.getFilesByUsername(userName);
		
	}
	
	
	
	
	
	//Gateway -> 8080
	//ileservice ->2222
	
	//userservice> 3333

}
