package com.e9pay.e9pay.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e9pay.e9pay.api.core.EntityService;
import com.e9pay.e9pay.api.entity.Member;
import com.e9pay.e9pay.api.service.MemberService;
import com.e9pay.e9pay.web.dto.MemberDto;
import com.e9pay.e9pay.web.mapper.DefaultDtoMapper;
import com.e9pay.e9pay.web.mapper.DtoMapper;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@RestController
@RequestMapping(MemberRestController.MEMBERS_URL)
public class MemberRestController extends BaseRestController<Member, MemberDto> {

    static final String MEMBERS_URL = "/data/v1/members";

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected EntityService<Member> getEntityService() {
        return memberService;
    }

    @Override
    protected DtoMapper<Member, MemberDto> getDtoMapper() {
        return new DefaultDtoMapper<>(Member.class, MemberDto.class);
    }
}
