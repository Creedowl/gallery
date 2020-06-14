package com.creedowl.gallery.dto;

import com.creedowl.gallery.model.UploadFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadFileRespDTO {
    private Long id;
    private Long userId;
    private Long count;
    private String originFilename;
    private String filename;
    private String link;

    public UploadFileRespDTO(UploadFile uploadFile) {
        this.id = uploadFile.getId();
        this.userId = uploadFile.getUserId();
        this.count = uploadFile.getCount();
        this.originFilename = uploadFile.getOriginFilename();
        this.filename = uploadFile.getFilename();
        this.link = "/s/" + this.filename;
    }
}
