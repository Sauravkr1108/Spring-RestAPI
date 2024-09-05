package com.filehandling.service;

import com.filehandling.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Boolean uploadFile(MultipartFile file) throws IOException;

    byte[] downloadFile(String imageName) throws IOException;

    Boolean saveProduct(Product product);

    String uploadFileWithData(MultipartFile file) throws IOException;
}
