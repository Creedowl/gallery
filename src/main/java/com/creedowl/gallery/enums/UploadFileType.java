package com.creedowl.gallery.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UploadFileType {
    JPG(1), PNG(2), GIF(3), WEBP(4);

    @EnumValue
    private final int type;

    UploadFileType(int type) {
        this.type = type;
    }
}
