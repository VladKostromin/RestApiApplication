package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.repository.EventRepository;
import com.vladkostromin.restapiapplication.repository.FileRepository;
import com.vladkostromin.restapiapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.nio.ByteBuffer;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    @Value("${aws.bucket}")
    private String bucket;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final S3AsyncClient asyncClient;

    public Mono<ByteBuffer> download(String keyName, ByteBuffer fileBytes) {
        return null;
    }



}
