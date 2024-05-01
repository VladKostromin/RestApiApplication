package com.vladkostromin.restapiapplication;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import com.vladkostromin.restapiapplication.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @Mock
    private FileRepository fileRepository;
    @InjectMocks
    private FileService fileService;

    private Mono<FileEntity> file;

    public FileServiceTest() {

    }

    @Test
    public void createFileTEst() {
    }



}
