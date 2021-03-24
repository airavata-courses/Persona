package com.google.drive.api.service;

import com.google.drive.api.dto.request.ShareDocumentRequest;
import com.google.drive.api.dto.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;


public interface FileService {


    void upload(MultipartFile[] files, String userName) throws Exception;

    ByteArrayOutputStream download(List<Long> ids, String userName) throws Exception;

    Set<DocumentResponse> getFilesByUsername(String userName) throws Exception;

    void deleteFileById(Long id) throws Exception;

    void shareDocuments(ShareDocumentRequest shareDocumentRequest);
}
