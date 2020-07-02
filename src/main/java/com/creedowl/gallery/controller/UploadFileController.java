package com.creedowl.gallery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.creedowl.gallery.dto.PageDTO;
import com.creedowl.gallery.dto.UploadFileListDTO;
import com.creedowl.gallery.dto.UploadFileRespDTO;
import com.creedowl.gallery.exception.CustomException;
import com.creedowl.gallery.model.UploadFile;
import com.creedowl.gallery.service.UploadFileService;
import com.creedowl.gallery.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class UploadFileController {
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final UploadFileService uploadFileService;

    public UploadFileController(ModelMapper modelMapper, UserService userService, UploadFileService uploadFileService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.uploadFileService = uploadFileService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "upload file", security = @SecurityRequirement(name = "bearerAuth"))
    public UploadFileRespDTO upload(@RequestParam("file") MultipartFile file) {
        var uploadFile = this.modelMapper.map(this.uploadFileService.upload(file), UploadFileRespDTO.class);
        uploadFile.setLink("/s/" + uploadFile.getFilename());
        return uploadFile;
    }

    @GetMapping(value = "/s/{filename}", produces = {
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    })
    public UrlResource getImg(@PathVariable String filename,
                              @RequestParam(defaultValue = "true") Boolean inc,
                              HttpServletResponse response) {
        var resource = this.uploadFileService.loadFile(filename, inc);
        if (resource == null) {
            response.setStatus(404);
        }
        return resource;
    }

    @DeleteMapping("/s/{filename}")
    @Operation(summary = "delete file by filename", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteImg(@PathVariable String filename) {
        var file = this.uploadFileService.getFile(filename);
        if (file == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "File Not Found");
        }
        var user = this.userService.getMe();
        if (!user.getId().equals(file.getUserId()) && !user.getIsAdmin()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "Permission Denied");
        }
        this.uploadFileService.getUploadFIleMapper().deleteById(file.getId());
    }

    @GetMapping("/files/{id}")
    @Operation(summary = "get file list", security = @SecurityRequirement(name = "bearerAuth"))
    public UploadFileListDTO getFileList(@PathVariable Integer id, PageDTO pageDTO) {
        var loginUser = this.userService.getMe();
        var user = this.userService.getById(id);
        if (!loginUser.getId().equals(user.getId()) && !loginUser.getIsAdmin()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        Page<UploadFile> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        return new UploadFileListDTO(this.uploadFileService.fileList(page, user.getId()), user);
    }
}
