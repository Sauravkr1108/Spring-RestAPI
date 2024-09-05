package com.filehandling.service.impl;

import com.filehandling.model.Product;
import com.filehandling.repository.ProductRepository;
import com.filehandling.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Boolean uploadFile(MultipartFile file) throws IOException
    {
        String fileName = file.getOriginalFilename();
        File saveFile = new File(uploadPath);
        if(!saveFile.exists())
            saveFile.mkdir();
        assert fileName != null;
        String storePath = uploadPath.concat(fileName);
        long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
        return (upload != 0);
    }

    @Override
    public byte[] downloadFile(String imageName) throws IOException
    {
        String fullPath = uploadPath.concat(imageName);
        InputStream inputStream = new FileInputStream(fullPath);
        return StreamUtils.copyToByteArray(inputStream);

    }

    @Override
    public Boolean saveProduct(Product product) {
        Product saveProduct = productRepository.save(product);
        return !ObjectUtils.isEmpty(saveProduct);
    }

    @Override
    public String uploadFileWithData(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        File saveFile = new File(uploadPath);
        String randomStr = UUID.randomUUID().toString();
        String removeExtension = FilenameUtils.removeExtension(fileName);
        String extension = FilenameUtils.getExtension(fileName);
        fileName = removeExtension + "_" + randomStr + "." + extension;
        if(!saveFile.exists())
            saveFile.mkdir();
        assert fileName != null;
        String storePath = uploadPath.concat(fileName);
        long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
        if(upload != 0)
            return fileName;
        else return null;
    }
}
