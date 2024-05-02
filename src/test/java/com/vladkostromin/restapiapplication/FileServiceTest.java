package com.vladkostromin.restapiapplication;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.enums.FileStatus;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import com.vladkostromin.restapiapplication.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    private FileEntity file;

    @BeforeEach
    public void init() {
        this.file = FileEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(FileStatus.AVALABLE)
                .fileName("testFile.png")
                .location("/some/location/folder/test")
                .build();
    }

    @Test
    public void getFileByIdTest() {
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(file));
        Mono<FileEntity> result = fileService.getFileById(1L);
        StepVerifier.create(result)
                .expectNextMatches(fileEntity -> fileEntity.equals(file))
                .verifyComplete();
        verify(fileRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getFileByFileNameTest() {
        when(fileRepository.findByFileName(file.getFileName())).thenReturn(Mono.just(file));
        Mono<FileEntity> result = fileService.getFileByName(file.getFileName());
        StepVerifier.create(result)
                .expectNextMatches(fileEntity -> fileEntity.getFileName().equals(file.getFileName()))
                .verifyComplete();
        verify(fileRepository, times(1)).findByFileName(file.getFileName());
    }

    @Test
    public void createFileTest() {
        when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(file));
        Mono<FileEntity> result = fileService.createFile(file);
        StepVerifier.create(result)
                .expectNextMatches(fileEntity -> fileEntity.equals(file))
                .verifyComplete();
        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Test
    public void updateFileTest() {
        FileEntity updatedFile = FileEntity.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusSeconds(2))
                .status(FileStatus.AVALABLE)
                .fileName("testFile.png")
                .location("/location/folder/updated")
                .build();;
        when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(updatedFile));
        Mono<FileEntity> result = fileService.updateFile(file);
        StepVerifier.create(result)
                .assertNext(fileEntity -> {
                    assertEquals("/location/folder/updated", fileEntity.getLocation());
                    assertNotNull(fileEntity.getCreatedAt());
                    assertTrue(fileEntity.getUpdatedAt().isAfter(this.file.getCreatedAt()));
                }).verifyComplete();
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    public void safeDeleteFileTest() {
        file.setStatus(FileStatus.REMOVED);
        when(fileRepository.findById(anyLong())).thenReturn(Mono.just(file));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(file));
        Mono<FileEntity> result = fileService.safeDeleteFile(1L);
        StepVerifier.create(result)
                .expectNextMatches(fileEntity -> fileEntity.getStatus().equals(file.getStatus()))
                .verifyComplete();
        verify(fileRepository, times(1)).save(file);
        verify(fileRepository, times(1)).findById(anyLong());
    }

    @Test
    public void deleteFileTest() {
        when(fileRepository.deleteById(anyLong())).thenReturn(Mono.empty());
        Mono<Void> result = fileService.deleteFile(1L);
        StepVerifier.create(result).verifyComplete();
        verify(fileRepository, times(1)).deleteById(anyLong());
    }
}
