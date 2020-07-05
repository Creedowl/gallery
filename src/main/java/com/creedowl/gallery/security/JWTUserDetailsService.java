package com.creedowl.gallery.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.creedowl.gallery.exception.CustomException;
import com.creedowl.gallery.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailsService implements UserDetailsService {
    // can't use UserService here, which will produce circular dependencies
    private final UserMapper userMapper;

    public JWTUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var wrapper = new QueryWrapper<com.creedowl.gallery.model.User>();
        wrapper.eq("username", username);
        var user = this.userMapper.selectOne(wrapper);
        if (user == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User Not Found");
        }
        if (user.getLocked()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "User account is locked");
        }
        var authority = user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER";
        return User.builder()
                .username(username)
                .password(user.getPassword())
                .authorities(authority)
                .accountExpired(false)
                .accountLocked(user.getLocked())
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
