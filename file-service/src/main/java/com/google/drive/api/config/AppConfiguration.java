package com.google.drive.api.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfiguration {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";

    private static final JsonFactory JSON_FACTORY = JacksonFactory
            .getDefaultInstance();

    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections
            .singletonList(DriveScopes.DRIVE);

    @Bean
    public Drive buildDrive() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport
                .newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                credential).setApplicationName(APPLICATION_NAME).build();
        System.out.println("driveService " + driveService);
        return driveService;
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws Exception {

        InputStream in = AppConfiguration.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("online").setApprovalPrompt("auto")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(8081).build();


        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
    }

}
