package com.creedowl.gallery.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.creedowl.gallery.model.UploadFile;
import com.creedowl.gallery.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UploadFileListDTO {
    private long total;
    private long size;
    private long current;
    private long pages;
    private long userId;
    private String username;
    private List<UploadFileRespDTO> records;

    public UploadFileListDTO(IPage<UploadFile> page, User user) {
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.pages = page.getPages();
        this.records = page.getRecords().stream().map(UploadFileRespDTO::new).collect(Collectors.toList());
//        this.records = page.getRecords();
        this.userId = user.getId();
        this.username = user.getUsername();
    }
}
