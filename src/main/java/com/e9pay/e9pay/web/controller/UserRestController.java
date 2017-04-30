package com.e9pay.e9pay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e9pay.e9pay.api.core.EntityService;
import com.e9pay.e9pay.api.entity.User;
import com.e9pay.e9pay.api.service.UserService;
import com.e9pay.e9pay.web.dto.UserDto;
import com.e9pay.e9pay.web.mapper.DefaultDtoMapper;
import com.e9pay.e9pay.web.mapper.DtoMapper;

/**
 * @author Vivek adhikari
 * @since 4/20/2017
 */
@RestController
@RequestMapping(UserRestController.USERS_URL)
public class UserRestController extends BaseRestController<User, UserDto> {

    static final String USERS_URL = "/data/v1/users";

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected EntityService<User> getEntityService() {
        return userService;
    }

    @Override
    protected DtoMapper<User, UserDto> getDtoMapper() {
        return new DefaultDtoMapper<>(User.class, UserDto.class);
    }


}