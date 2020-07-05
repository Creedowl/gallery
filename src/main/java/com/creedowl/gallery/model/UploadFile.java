package com.creedowl.gallery.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.creedowl.gallery.enums.UploadFileType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadFile {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long count;
    private String originFilename;
    private String filename;
    private UploadFileType type;
    @TableLogic
    private Boolean deleted;

    public UploadFile(Long userId, Long count, String originFilename, String filename, String type) {
        this.userId = userId;
        this.count = count;
        this.originFilename = originFilename;
        this.filename = filename;
        switch (type) {
            case "jpg":
            case "jpeg":
                this.type = UploadFileType.JPG;
                break;
            case "png":
                this.type = UploadFileType.PNG;
                break;
            case "gif":
                this.type = UploadFileType.GIF;
                break;
            case "webp":
                this.type = UploadFileType.WEBP;
                break;
        }
    }

    public void inc() {
        this.count++;
    }
}
