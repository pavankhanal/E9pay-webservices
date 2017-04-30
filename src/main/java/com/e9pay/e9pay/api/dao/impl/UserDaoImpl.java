package com.e9pay.e9pay.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.e9pay.e9pay.api.core.BaseEntityDao;
import com.e9pay.e9pay.api.dao.UserDao;
import com.e9pay.e9pay.api.entity.User;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@Repository
public class UserDaoImpl extends BaseEntityDao<User> implements UserDao {

    public UserDaoImpl(){
        super(User.class);
    }

    @Override
    public void doSomething() {

    }
}
