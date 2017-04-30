package com.e9pay.e9pay.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e9pay.e9pay.api.core.BaseEntityService;
import com.e9pay.e9pay.api.core.EntityDao;
import com.e9pay.e9pay.api.dao.MemberDao;
import com.e9pay.e9pay.api.entity.Member;
import com.e9pay.e9pay.api.service.MemberService;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Service("memberService")
@Transactional
public class MemberServiceImpl extends BaseEntityService<Member> implements MemberService {

    private final MemberDao memberDao;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    protected EntityDao<Member> getEntityDao() {
        return memberDao;
    }
}
