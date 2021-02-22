package com.file.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

@Component
public class GoogleDriveUtils {

	@Autowired
	private Drive drive;

	public String upload(java.io.File file, String userName, String fileName,String contentType)
			throws Exception {
		
		File fileMetadata = new File();
		fileMetadata.setName(userName + "_" + fileName);
		FileContent mediaContent = new FileContent(contentType, file);
		File driveFile = drive.files().create(fileMetadata, mediaContent)
				.setFields("id").execute();
		System.out.println("File ID: " + driveFile.getId());
		return driveFile.getId();
	}

	public ByteArrayOutputStream download(String fileId) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		drive.files().get(fileId)
	    .executeMediaAndDownloadTo(outputStream);
		return outputStream;
	}

}
