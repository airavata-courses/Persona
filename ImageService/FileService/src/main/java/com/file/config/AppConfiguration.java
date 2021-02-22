package com.file.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@Configuration
public class AppConfiguration {

	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";

	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();

	private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(
			"/Users/suresh/documents/GoogleDrive");

	private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

	private static final List<String> SCOPES = Collections
			.singletonList(DriveScopes.DRIVE);

	@Bean
	public Drive buildDrive() throws Exception {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport
				.newTrustedTransport();
		Credential credential = getCredentials(HTTP_TRANSPORT);
		Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				credential).setApplicationName(APPLICATION_NAME).build();
		System.out.println("driveService "+driveService);
		return driveService;
	}

	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
			throws Exception {

		java.io.File clientSecretFilePath = new java.io.File(
				CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);

		if (!clientSecretFilePath.exists()) {
			throw new FileNotFoundException("Please copy "
					+ CLIENT_SECRET_FILE_NAME //
					+ " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
		}

		// Load client secrets.
		InputStream in = new FileInputStream(clientSecretFilePath);

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(
						new FileDataStoreFactory(CREDENTIALS_FOLDER))
				.setAccessType("offline").setApprovalPrompt("auto").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder()
				.setPort(8081).build();
		return new AuthorizationCodeInstalledApp(flow, receiver)
				.authorize("user");
	}

}
