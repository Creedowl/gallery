package com.creedowl.gallery.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.creedowl.gallery.model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserListDTO {
    private long total;
    private long size;
    private long current;
    private long pages;
    private List<UserRespDTO> records;

    public UserListDTO(IPage<User> page) {
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.pages = page.getPages();
        this.records = page.getRecords().stream().map(UserRespDTO::new).collect(Collectors.toList());
    }
}
