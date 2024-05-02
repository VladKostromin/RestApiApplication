package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.dto.FileDto;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.enums.FileStatus;
import com.vladkostromin.restapiapplication.mapper.FileMapper;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public Mono<FileEntity> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    public Mono<FileEntity> createFile(FileEntity file) {
        return fileRepository.save(
                file.toBuilder()
                        .fileName(file.getFileName())
                        .location(file.getLocation())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .status(FileStatus.AVALABLE)
                .build());
    }

    public Mono<FileEntity> getFileByName(String fileName) {
        return fileRepository.findByFileName(fileName);
    }



    public Mono<Void> deleteFile(Long id) {
        return fileRepository.deleteById(id);
    }

    public Mono<FileEntity> safeDeleteFile(Long id) {
        return fileRepository.findById(id)
                .flatMap(file -> {
                    file.setStatus(FileStatus.REMOVED);
                    return fileRepository.save(file);
                });
    }

    public Mono<FileEntity> updateFile(FileEntity file) {
        file.setUpdatedAt(LocalDateTime.now());
        return fileRepository.save(file);
    }

}
