package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.config.StorageProperties;
import com.alpha53.virtualteacher.exceptions.StorageException;
import com.alpha53.virtualteacher.exceptions.StorageFileNotFoundException;
import com.alpha53.virtualteacher.models.Solution;
import com.alpha53.virtualteacher.services.contracts.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String uniqueFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check (Spring framework comment).
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return extractFilePath(destinationFile);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try (Stream<Path> paths = Files.walk(this.rootLocation, 1))
        {
            return paths
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public void delete(String filePath) {

        try {
            Path path = Paths.get("src/main/resources/static" + filePath);
            Files.deleteIfExists(path);
        } catch (IOException e){
            throw new StorageException(String.format("Unable to delete file %s.", filePath));
        }
    }

    @Override
    public Resource loadAsResource(String filePath) {
        try {
            Path path = Paths.get("src/main/resources/static" + filePath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filePath);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filePath, e);
        }
    }

    @Override
    public void deleteAll(List<Solution> solutions) {
        for (Solution solution : solutions) {
            delete(solution.getSolutionUrl());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(rootLocation)){
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


    private String extractFilePath(Path fileLocation) {
        String filePath = fileLocation.toString();
        int startingPoint = filePath.indexOf("assets");
        return filePath.substring(startingPoint-1);
    }

    private String generateUniqueFileName(String originalFilename) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String timestamp = dateFormat.format(new Date());
        String randomChars = UUID.randomUUID().toString().replaceAll("-", "");

        int dotIndex = originalFilename.lastIndexOf(".");
        String fileExtension = dotIndex > 0 ? originalFilename.substring(dotIndex) : "";
        return originalFilename.substring(0, dotIndex) + "-" + timestamp + "-" + randomChars + fileExtension;
    }
}
