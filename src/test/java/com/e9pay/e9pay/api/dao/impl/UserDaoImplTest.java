package com.e9pay.e9pay.api.dao.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.springframework.beans.factory.annotation.Autowired;

import com.e9pay.e9pay.api.dao.AbstractTransactionalDaoTest;
import com.e9pay.e9pay.api.dao.UserDao;
import com.e9pay.e9pay.api.entity.User;
import org.testng.annotations.Test;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public class UserDaoImplTest extends AbstractTransactionalDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testSaveUser() {
        final User user = new User();
        user.setId(1l);
        user.setUserNameInKorean("비벡 아드 카리");
        user.setUserName("Vivek Adhikari");
        user.setEmail("thevivek2@gmail.com");
        user.setMobile1("9841527953");
        user.setRole("Lead");
        userDao.save(user);
        assertThat(userDao.findAll(), hasSize(1));

    }
}
