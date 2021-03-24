package com.google.drive.api.service.impl;


import com.google.drive.api.entity.Document;
import com.google.drive.api.entity.User;
import com.google.drive.api.exception.AppException;
import com.google.drive.api.exception.ResourceNotFoundException;
import com.google.drive.api.mapper.DocumentMapper;
import com.google.drive.api.repo.DocumentRepo;
import com.google.drive.api.repo.UserRepo;
import com.google.drive.api.dto.request.ShareDocumentRequest;
import com.google.drive.api.dto.response.DocumentResponse;
import com.google.drive.api.service.FileService;
import com.google.drive.api.util.GoogleDriveUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private GoogleDriveUtils googleDrive;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    @Transactional
    public void upload(MultipartFile[] files, String userName) throws Exception {

        User user = userRepo.findByUserName(userName).orElseGet(() -> createNewUser(userName));

        for (MultipartFile file : files) {
            File targetFile = File.createTempFile(file.getOriginalFilename(), "");
            try (OutputStream outStream = new FileOutputStream(targetFile)) {
                outStream.write(file.getBytes());
                String fileId = googleDrive.upload(targetFile, userName, file.getOriginalFilename(),
                        file.getContentType());
                Document document = new Document();
                document.setFileId(fileId);
                document.setFileName(file.getOriginalFilename());
                document.setOwner(user);
                Document dbDocument = documentRepo.save(document);

                user.addDocument(dbDocument);

            } catch (Exception e) {
                logger.error("Error while upload document to drive ", e);
                throw new AppException("Error while upload document to drive ");
            } finally {
                targetFile.delete();
            }
            userRepo.save(user);
        }
    }

    @Override
    public void deleteFileById(Long id) throws Exception {
        Document dbDocument = documentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found id :" + id ));

        googleDrive.deleteFile(dbDocument.getFileId());
        documentRepo.deleteById(id);
    }

    @Override
    public void shareDocuments(ShareDocumentRequest shareDocumentRequest) {

        User userToShare = userRepo.findByUserName(shareDocumentRequest.getUsernameToShare())
                .orElseThrow(() -> new ResourceNotFoundException("User not found username :" + shareDocumentRequest.getUsernameToShare()));

        documentRepo.findAllById(shareDocumentRequest.getDocumentIds()).forEach(userToShare::addDocument);
        userRepo.save(userToShare);

    }

    @Override
    public ByteArrayOutputStream download(List<Long> ids, String userName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (Long id : ids) {
                    Document doc = documentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found"));
                    ZipEntry entry = new ZipEntry(doc.getFileName());
                    zos.putNextEntry(entry);
                    zos.write(googleDrive.download(doc.getFileId()).toByteArray());
                }
            }
            return baos;
        } catch (Exception e) {
            logger.error("Error while download document ", e);
            throw new AppException("Error while download document ");
        }
    }

    @Override
    public Set<DocumentResponse> getFilesByUsername(String userName) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found username :" + userName));

        Set<DocumentResponse> documentResponses = user.getDocuments().stream()
                .map(DocumentMapper::documentToDocumentResponse)
                .collect(Collectors.toSet());

        documentResponses.forEach(documentResponse -> documentResponse.setData(googleDrive.getByteArray(documentResponse.getFileId())));
        return documentResponses;

    }

    private User createNewUser(String userName) {
        User newUser = new User();
        newUser.setUserName(userName);
        return userRepo.save(newUser);
    }

}
