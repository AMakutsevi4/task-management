package ru.management.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSavedService {

    @Value("${upload.dir}")
    private String uploadDir;

    public void saveFile(Long taskId, MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String filename = "task_" + taskId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
    }

    public File loadFile(Long taskId) throws IOException {
        Path dirPath = Paths.get(uploadDir).normalize();

        Path filePath = Files.list(dirPath)
                .filter(f -> f.getFileName().toString().startsWith("task_" + taskId + "_"))
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("Файл для задачи " + taskId + " не найден"));

        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.isReadable()) {
            throw new FileNotFoundException("Файл недоступен для чтения: " + filePath);
        }
        return resource.getFile();
    }
}
