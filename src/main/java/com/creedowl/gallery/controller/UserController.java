package com.creedowl.gallery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.creedowl.gallery.dto.PageDTO;
import com.creedowl.gallery.dto.UserListDTO;
import com.creedowl.gallery.dto.UserRespDTO;
import com.creedowl.gallery.model.User;
import com.creedowl.gallery.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/me")
    @Operation(summary = "show the login user's detail", security = @SecurityRequirement(name = "bearerAuth"))
    public UserRespDTO getMe() {
        return modelMapper.map(this.userService.getMe(), UserRespDTO.class);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "show the login user's detail", security = @SecurityRequirement(name = "bearerAuth"))
    public UserRespDTO getUserById(@PathVariable Integer id) {
        return modelMapper.map(this.userService.getById(id), UserRespDTO.class);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "get user list", security = @SecurityRequirement(name = "bearerAuth"))
    public UserListDTO getUsers(PageDTO pageDTO) {
        Page<User> page = new Page<>(pageDTO.getCurrent(), pageDTO.getSize());
        return new UserListDTO(this.userService.page(page));
    }
}
