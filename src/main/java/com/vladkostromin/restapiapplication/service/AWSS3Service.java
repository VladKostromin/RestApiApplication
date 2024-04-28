package com.vladkostromin.restapiapplication.service;

import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.enums.FileStatus;
import com.vladkostromin.restapiapplication.exception.DownloadException;
import com.vladkostromin.restapiapplication.exception.UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AWSS3Service {

    @Value("${aws.bucket.name}")
    private String bucket;
    @Value("${aws.localFolder}")
    private String localFolder;

    private final S3AsyncClient s3AsyncClient;
    private final FileService fileService;


    public Mono<FileEntity> download(FileEntity fileEntity) {
        String fileName = fileEntity.getFileName();
        GetObjectRequest getObjectAclRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        return Mono.fromFuture(() ->
                s3AsyncClient.getObject(getObjectAclRequest, AsyncResponseTransformer.toFile(Paths.get(localFolder + fileName))))
                .flatMap(response -> {
                    fileEntity.setFileName(fileName);
                    fileEntity.setLocation(localFolder + "/" + fileName);
                    fileEntity.setStatus(FileStatus.AVALABLE);
                    return fileService.updateFile(fileEntity);
                })
                .onErrorResume(e -> Mono.error(new DownloadException("Download filed", "ERROR_CODE_DOWNLOAD_FAIL")));
    }


    public Mono<FileEntity> upload(FilePart filePart) {
        String fileKey = filePart.filename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();
        return filePart.content()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                }).collectList().map(this::concatenateByteArrays)
                .flatMap(bytes -> Mono.fromFuture(() ->
                        s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(bytes)))
                        .thenReturn(new FileEntity(filePart.filename(), "s3://" + bucket + "/" + fileKey, FileStatus.AVALABLE)))
                .onErrorResume(e -> {
                            log.error("Error during upload: {}", e.getMessage(), e);
                            return Mono.error(new UploadException("Upload filed", "ERROR_CODE_UPLOAD_FAIL"));
                        });
    }


    private byte[] concatenateByteArrays(List<byte[]> byteArrays) {
        int totalBytes = byteArrays.stream().mapToInt(a -> a.length).sum();
        byte[] bytes = new byte[totalBytes];
        int currentIndex = 0;
        for (byte[] byteArray : byteArrays) {
            System.arraycopy(byteArray, 0, bytes, currentIndex, byteArray.length);
            currentIndex += byteArray.length;
        }
        return bytes;
    }

}
