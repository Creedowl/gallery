package com.creedowl.gallery.controller;

import com.creedowl.gallery.dto.TokenDTO;
import com.creedowl.gallery.dto.UserDataDTO;
import com.creedowl.gallery.dto.UserRespDTO;
import com.creedowl.gallery.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public AuthController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserRespDTO register(@RequestBody UserDataDTO registerUser) {
        var user = this.userService.register(registerUser);
        return this.modelMapper.map(user, UserRespDTO.class);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody UserDataDTO loginUser){
        return new TokenDTO(this.userService.login(loginUser));
    }
}
