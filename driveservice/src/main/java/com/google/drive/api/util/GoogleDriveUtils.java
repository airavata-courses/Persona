package com.google.drive.api.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.drive.api.exception.AppException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Get;
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

	public void deleteFile(String fileId) throws Exception {
		drive.files().delete(fileId).execute();
	}

	public byte[] getByteArray(String fileId) {

			try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
				drive.files().get(fileId)
						.executeMediaAndDownloadTo(outputStream);
				byte[] bytes = outputStream.toByteArray();

				outputStream.reset();
				System.out.println("Print Byte: ");
				for(byte curByte: bytes) {
					System.out.println(curByte);
				}
				return bytes;
			}catch (Exception e ) {
               throw new AppException("Can't read file from driver");
			}

}
}
