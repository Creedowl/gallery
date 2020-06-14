package com.creedowl.gallery.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.creedowl.gallery.exception.CustomException;
import com.creedowl.gallery.mapper.UploadFIleMapper;
import com.creedowl.gallery.model.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UploadFileService {
    @Value("${file.suffix}")
    private ArrayList<String> fileSuffix;

    @Value("${file.uploadPath}")
    private String uploadPath;

    private Path path;

    private final UploadFIleMapper uploadFIleMapper;

    private final UserService userService;

    @PostConstruct
    public void init() {
        this.path = Paths.get(this.uploadPath);
    }

    public UploadFileService(UploadFIleMapper uploadFIleMapper, UserService userService) {
        this.uploadFIleMapper = uploadFIleMapper;
        this.userService = userService;
    }

    public UploadFile upload(MultipartFile file) {
        var filename = file.getOriginalFilename();
        if (filename == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "filename does not exist");
        }
        var dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "file type incorrect");
        }
        var suffix = filename.substring(dotIndex + 1).toLowerCase();
        if (!this.fileSuffix.contains(suffix)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "file type incorrect");
        }
        var uuidFilename = UUID.randomUUID().toString();
        try {
            Files.copy(file.getInputStream(), this.path.resolve(uuidFilename));
        } catch (IOException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "failed to store file");
        }
        var user = this.userService.getMe();
        var uploadFile = new UploadFile(user.getId(), 0L, filename, uuidFilename, suffix);
        this.uploadFIleMapper.insert(uploadFile);
        return uploadFile;
    }

    public UrlResource loadFile(String filename) {
        var wrapper = new QueryWrapper<UploadFile>();
        wrapper.eq("filename", filename);
        var uploadFile = this.uploadFIleMapper.selectOne(wrapper);
        if (uploadFile == null) {
            return null;
        }
        uploadFile.inc();
        this.uploadFIleMapper.updateById(uploadFile);
        var filePath = this.path.resolve(filename).normalize();
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public IPage<UploadFile> fileList(Page<UploadFile> page, Long userId) {
        var wrapper = new QueryWrapper<UploadFile>();
        wrapper.eq("user_id", userId);
        return this.uploadFIleMapper.selectPage(page, wrapper);
    }
}
