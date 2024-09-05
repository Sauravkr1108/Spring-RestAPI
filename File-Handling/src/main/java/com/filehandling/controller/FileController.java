package com.filehandling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filehandling.model.Product;
import com.filehandling.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file)
    {
        try {
            Boolean uploadFile = fileService.uploadFile(file);
            if(uploadFile)
                return new ResponseEntity<>("Upload successfully", HttpStatus.CREATED);
            else return new ResponseEntity<>("Upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-data")
    public ResponseEntity<?> uploadFileWithData(@RequestParam String product, @RequestParam MultipartFile file)
    {
//        log.info("Product:{}", product);
//        log.info("file:{}", file);
        List<String> extension = Arrays.asList("jpeg", "png", "jpg");
        if(file.isEmpty())
            return new ResponseEntity<>("Please upload image", HttpStatus.PARTIAL_CONTENT);
        else {
            String orgFile = file.getOriginalFilename();
            String fileExtension = FilenameUtils.getExtension(orgFile);
            if(!extension.contains(fileExtension))
                return new ResponseEntity<>("Please upload jpeg/png/jpg image", HttpStatus.BAD_REQUEST);
            else {
                try {
                    String fileName = fileService.uploadFileWithData(file);
                    if(!fileName.isEmpty()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Product productObj = objectMapper.readValue(product, Product.class);
                        productObj.setImageName(fileName);
                        Boolean saveProduct = fileService.saveProduct(productObj);
                        if(saveProduct)
                            return new ResponseEntity<>("Product & Image upload successfully", HttpStatus.CREATED);
                    }
                    else
                        return new ResponseEntity<>("Upload failed!!", HttpStatus.NO_CONTENT);
                } catch (Exception e) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>("Upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName)
    {
        try{
            byte[] downloadFile = fileService.downloadFile(fileName);
            String contentType = getContentType(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
//            headers.setContentLength(fileName.length());
            headers.setContentDispositionFormData("attachment", fileName);
            return ResponseEntity.ok().headers(headers).body(downloadFile);
        } catch (IOException e) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
    }

    public String getContentType(String fileName)
    {
        String extension = FilenameUtils.getExtension(fileName);
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
            case "txt" -> "text/plan";
            case "png" -> "image/png";
            case "jpeg" -> "image/jpeg";
            default -> "application/octet-stream";
        };
    }

}
