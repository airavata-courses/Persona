package com.file.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.Document;
import com.file.repo.DocumentRepo;
import com.file.response.DocumentResponse;
import com.file.service.FileService;
import com.file.util.GoogleDriveUtils;

@Service
public class FileServiceImpl implements FileService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(FileServiceImpl.class);

	@Autowired
	private GoogleDriveUtils googleDrive;

	@Autowired
	private DocumentRepo documentRepo;

	@Override
	public String upload(MultipartFile[] files, String userName)
			throws Exception {
		System.out.println("I am working here!!" + ":" + files.length + ":" + files);
		for (MultipartFile file : files) {
			System.out.println(files);
			File targetFile = File.createTempFile(file.getOriginalFilename(),
					"");
			try (OutputStream outStream = new FileOutputStream(targetFile)) {
				outStream.write(file.getBytes());
				String fileId = googleDrive.upload(targetFile, userName,
						file.getOriginalFilename(), file.getContentType());
				Document document = new Document();
				document.setFileId(fileId);
				document.setFileName(file.getOriginalFilename());
				document.setUserName(userName);
				document.setData(file.getBytes());
				documentRepo.save(document);
			} catch (Exception e) {
				logger.error("Error while upload document to drive ", e);
			}
			targetFile.delete();
		}
		return "SUCCESS";
	}

	
	
	@Override
	public ByteArrayOutputStream download(List<Long> ids, String userName)
			throws Exception {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ZipOutputStream zos = new ZipOutputStream(baos)) {
				for (Long id : ids) {
					Optional<Document> document = documentRepo.findById(id);
					if (document.isPresent()) {
						Document doc = document.get();
						ZipEntry entry = new ZipEntry(doc.getFileName());
						zos.putNextEntry(entry);
						zos.write(googleDrive.download(doc.getFileId())
								.toByteArray());
					}
				}
			} catch (Exception e) {

			}
			return baos;
		} catch (Exception e) {

		}
		return null;
	}
	
	

	@Override
	public Set<DocumentResponse> getFilesByUsername(String userName) {
		Set<DocumentResponse> documents =new HashSet<>();
		Set<Document> set = documentRepo.findByUserName(userName);
		set.stream().forEach( e->{
			DocumentResponse doc =new DocumentResponse();
			BeanUtils.copyProperties(e, doc);
			documents.add(doc);
		});
		return documents;
	}
}
