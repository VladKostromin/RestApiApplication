package com.vladkostromin.restapiapplication.rest;

import com.vladkostromin.restapiapplication.dto.FileDto;
import com.vladkostromin.restapiapplication.entity.FileEntity;
import com.vladkostromin.restapiapplication.mapper.FileMapper;
import com.vladkostromin.restapiapplication.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final FileMapper fileMapper;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'moderator:read')")
    public Mono<FileDto> getFile(@PathVariable Long id) {
        return fileService.getFileById(id);
    }
    @PostMapping("/create/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Mono<FileDto> saveFile(@RequestBody FileEntity fileEntity) {
        return fileService.createFile(fileEntity).map(fileMapper::map);
    }
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Mono<FileDto> updateFile(@RequestBody FileEntity fileEntity) {
        return fileService.updateFile(fileEntity).map(fileMapper::map);
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public Mono<Void> deleteFile(@PathVariable Long id) {
        return fileService.deleteFile(id);
    }

    @PostMapping("/safe-delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'moderator:delete')")
    public Mono<FileDto> safeDelete(@PathVariable Long id) {
        return fileService.safeDeleteFile(id).map(fileMapper::map);
    }

}
