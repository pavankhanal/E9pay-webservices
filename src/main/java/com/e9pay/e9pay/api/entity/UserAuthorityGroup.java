package com.e9pay.e9pay.api.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "user_authority_group")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_user_authority_group")
public class UserAuthorityGroup extends BaseEntity {

    @Column(name = "auth_id")
    private String authId;

    private String name;

    @Column(name = "enable_OTP")
    private boolean enableOTP;

    @OneToOne
    @JoinColumn(name = "user_type_code_id")
    private Code userGroup;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "authority_group_menu_item",
        joinColumns = @JoinColumn(name = "user_authority_group_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_group_menu_item_id")
    )
    List<AuthorityGroupMenuItem> authorityGroupMenuItems;
}
