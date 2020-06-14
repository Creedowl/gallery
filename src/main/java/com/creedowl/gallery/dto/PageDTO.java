package com.creedowl.gallery.dto;

import lombok.Data;

@Data
public class PageDTO {
    private Integer current = 0;
    private Integer size = 5;
}
