package com.google.drive.api.mapper;

import com.google.drive.api.dto.response.DocumentResponse;
import com.google.drive.api.entity.Document;


public class DocumentMapper {

    public static DocumentResponse documentToDocumentResponse(Document document) {
        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setId(document.getId());
        documentResponse.setFileId(document.getFileId());
        documentResponse.setData(document.getData());
        documentResponse.setFileName(document.getFileName());
        documentResponse.setUserName(document.getOwner().getUserName());
        return documentResponse;
    }
}
