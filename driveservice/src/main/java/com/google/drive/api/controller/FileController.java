package com.google.drive.api.controller;

import com.google.drive.api.dto.request.ShareDocumentRequest;
import com.google.drive.api.dto.response.DocumentResponse;
import com.google.drive.api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public String get() {
        return "SUCCESS";
    }

    @PostMapping("/upload/{username}")
    public ResponseEntity uploadFile(
            @RequestPart("files") MultipartFile[] files,
            @PathVariable("username") String userName) throws Exception {
        fileService.upload(files, userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/download")
    public void uploadFile(
            @RequestBody List<Long> ids,
            @RequestParam("username") String userName, HttpServletResponse response) throws Exception {

        ByteArrayOutputStream outputStream = fileService.download(ids, userName);
        response.getOutputStream().write(outputStream.toByteArray());
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=sample.zip");
    }

    @GetMapping("/getFiles")
    public ResponseEntity<Set<DocumentResponse>> getFilesByUsername(@RequestParam("username") String userName) throws Exception {
        return new ResponseEntity<>(fileService.getFilesByUsername(userName), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        fileService.deleteFileById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shareDocument")
    public ResponseEntity shareDocument(@RequestBody @Valid ShareDocumentRequest shareDocumentRequest) {
        fileService.shareDocuments(shareDocumentRequest);
        return ResponseEntity.ok().build();
    }

}
