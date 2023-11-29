package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.Solution;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);
    void delete(String filename);

    Resource loadAsResource(String filename);
    void deleteAll(List<Solution> solutions);

    void deleteAll();
}
