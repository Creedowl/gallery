package com.creedowl.gallery.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String password;
    private String newPassword;
    private Boolean isAdmin;
    private Boolean locked;
}
