package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@javax.persistence.Entity
@Table(name = "e9pay_user")
@Data
public class User extends BaseEntity {

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_name_in_korean")
    private String userNameInKorean;

    @Column(name = "mobile1", length = 50)
    private String mobile1;

    @Column(name = "mobile2", length = 50)
    private String mobile2;

    @Column(length = 100, unique = true)
    private String email;

    @Lob
    private String password;

    private String role;

    @Column(name = "connect_ip")
    private String connectIp;

    @Column(length = 25)
    private String status;

    @OneToOne
    @JoinColumn(name = "user_type_code_id")
    private Code userGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_authority_group_id")
    private UserAuthorityGroup userAuthorityGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

}
