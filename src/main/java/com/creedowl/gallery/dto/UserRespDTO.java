package com.creedowl.gallery.dto;

import com.creedowl.gallery.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRespDTO {
    private Long id;
    private String username;
    private Boolean isAdmin;
    private Boolean locked;

    public UserRespDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.isAdmin = user.getIsAdmin();
        this.locked = user.getLocked();
    }
}
