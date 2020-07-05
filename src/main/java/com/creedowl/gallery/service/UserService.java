package com.creedowl.gallery.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.creedowl.gallery.dto.UserDataDTO;
import com.creedowl.gallery.dto.UserUpdateDTO;
import com.creedowl.gallery.events.publisher.UserLockPublisher;
import com.creedowl.gallery.exception.CustomException;
import com.creedowl.gallery.mapper.UserMapper;
import com.creedowl.gallery.model.User;
import com.creedowl.gallery.security.JWTUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserLockPublisher userLockPublisher;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserService(UserMapper userMapper, ModelMapper modelMapper, JWTUtil jwtUtil,
                       AuthenticationManager authenticationManager, UserLockPublisher userLockPublisher) {
        this.userMapper = userMapper;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userLockPublisher = userLockPublisher;
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
        user.setPassword(this.jwtUtil.encodePassword(user.getPassword()));
        user.setIsAdmin(false);
        this.save(user);
        return user;
    }

    public String login(UserDataDTO loginUser) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                loginUser.getPassword()));
        this.logger.info("user login: {}", loginUser.getUsername());
        return this.jwtUtil.generateJWT(this.modelMapper.map(loginUser, User.class));
    }

    public User getMe() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return this.getUserByUsername(auth.getName());
    }

    public User updateUserInfo(Long id, UserUpdateDTO userUpdateDTO) {
        var user = this.getById(id);
        var currentUser = this.getMe();
        if (currentUser.getIsAdmin()) {
            if (userUpdateDTO.getIsAdmin() != null) {
                user.setIsAdmin(userUpdateDTO.getIsAdmin());
            }
            if (userUpdateDTO.getLocked() != null) {
                user.setLocked(userUpdateDTO.getLocked());
                this.userLockPublisher.publish(user);
            }
        }
        // reset password
        if (userUpdateDTO.getPassword() != null && userUpdateDTO.getNewPassword() != null) {
            if ((currentUser.getId().equals(id) &&
                    this.jwtUtil.checkPassword(userUpdateDTO.getPassword(), user.getPassword())) ||
                    currentUser.getIsAdmin()) {
                user.setPassword(this.jwtUtil.encodePassword(userUpdateDTO.getNewPassword()));
            } else {
                throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "password incorrect");
            }
        }
        this.updateById(user);
        return user;
    }
}
