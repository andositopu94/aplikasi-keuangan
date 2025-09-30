package brajaka.demo.service;

import brajaka.demo.config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStrorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStrorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could Not Create Upload", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
    public Resource loadFileAsResource(String filename){
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()){
                return resource;
            }else {
                throw new RuntimeException("File not found: " + filename);
            }
        }catch (MalformedURLException e){
            throw new RuntimeException("Error reading file", e);
        }
    }
}
