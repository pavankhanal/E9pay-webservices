package com.e9pay.e9pay.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.e9pay.e9pay.api.core.BaseEntityDao;
import com.e9pay.e9pay.api.dao.MemberDao;
import com.e9pay.e9pay.api.entity.Member;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Repository
public class MemberDaoImpl extends BaseEntityDao<Member> implements MemberDao {

    public MemberDaoImpl() {
        super(Member.class);
    }
}
