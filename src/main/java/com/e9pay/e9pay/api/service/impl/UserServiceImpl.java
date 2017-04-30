package com.e9pay.e9pay.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e9pay.e9pay.api.core.BaseEntityService;
import com.e9pay.e9pay.api.core.EntityDao;
import com.e9pay.e9pay.api.dao.UserDao;
import com.e9pay.e9pay.api.entity.User;
import com.e9pay.e9pay.api.service.UserService;

/**
 * @author Vivek Adhikari
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseEntityService<User> implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected EntityDao<User> getEntityDao() {
        return userDao;
    }
}
