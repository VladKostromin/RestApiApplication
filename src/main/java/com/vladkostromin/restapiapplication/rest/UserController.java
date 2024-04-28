package com.vladkostromin.restapiapplication.rest;

import com.vladkostromin.restapiapplication.dto.FileDto;
import com.vladkostromin.restapiapplication.dto.UserDto;
import com.vladkostromin.restapiapplication.entity.EventEntity;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.entity.UserEntity;
import com.vladkostromin.restapiapplication.enums.EventStatus;
import com.vladkostromin.restapiapplication.mapper.FileMapper;
import com.vladkostromin.restapiapplication.service.AWSS3Service;
import com.vladkostromin.restapiapplication.service.EventService;
import com.vladkostromin.restapiapplication.service.FileService;
import com.vladkostromin.restapiapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AWSS3Service awss3Service;
    private final FileService fileService;
    private final EventService eventService;


    private final FileMapper fileMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public Mono<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('user:upload', 'manager:upload', 'admin:upload')")
    public Mono<FileDto> uploadFile(@RequestPart("file") FilePart file, Authentication authentication) {
        return Mono.zip(userService.getUserByUsername(authentication.getName()),
                        awss3Service.upload(file).flatMap(fileService::createFile))
                .flatMap(tuples -> {
                    UserEntity userEntity = tuples.getT1();
                    FileEntity fileEntity = tuples.getT2();
                    EventEntity eventEntity = EventEntity.builder()
                                    .user(userEntity)
                                    .file(fileEntity)
                                    .userId(userEntity.getId())
                                    .fileId(fileEntity.getId())
                                    .status(EventStatus.UPLOADED)
                                    .build();
                    return eventService.createEvent(eventEntity).thenReturn(fileEntity);
                }).map(fileMapper::map);
    }

    @GetMapping("/download/{fileKey}")
    public Mono<FileDto> download(@PathVariable String fileKey, Authentication authentication) {
        return Mono.zip(fileService.getFileByName(fileKey),
                userService.getUserByUsername(authentication.getName()))
                .flatMap(tuples -> {
                    FileEntity fileEntity = tuples.getT1();
                    UserEntity userEntity = tuples.getT2();
                    EventEntity eventEntity = EventEntity.builder()
                            .user(userEntity)
                            .file(fileEntity)
                            .userId(userEntity.getId())
                            .fileId(fileEntity.getId())
                            .status(EventStatus.DOWNLOADED)
                            .build();
                    return eventService.createEvent(eventEntity).thenReturn(fileEntity);
                }).flatMap(awss3Service::download).map(fileMapper::map);
    }


}
