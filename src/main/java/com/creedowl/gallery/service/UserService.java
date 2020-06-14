package com.creedowl.gallery.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.creedowl.gallery.dto.UserDataDTO;
import com.creedowl.gallery.exception.CustomException;
import com.creedowl.gallery.mapper.UserMapper;
import com.creedowl.gallery.model.User;
import com.creedowl.gallery.security.JWTUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

@Service
public class UserService implements IService<User> {
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserMapper userMapper, ModelMapper modelMapper, JWTUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean saveBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<User> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(User entity) {
        return false;
    }

    @Override
    public User getOne(Wrapper<User> queryWrapper, boolean throwEx) {
        return this.userMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public <V> V getObj(Wrapper<User> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<User> getBaseMapper() {
        return this.userMapper;
    }

    public User getUserByUsername(String username) {
        var wrapper = new QueryWrapper<User>();
        wrapper.eq("username", username);
        return this.userMapper.selectOne(wrapper);
    }

    public User register(UserDataDTO registerUser) {
        if (this.getUserByUsername(registerUser.getUsername()) != null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User Existed");
        }
        var user = this.modelMapper.map(registerUser, User.class);
        var encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setIsAdmin(false);
        this.save(user);
        return user;
    }

    public String login(UserDataDTO loginUser) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                loginUser.getPassword()));
        return this.jwtUtil.generateJWT(this.modelMapper.map(loginUser, User.class));
    }

    public User getMe() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return this.getUserByUsername(auth.getName());
    }
}
