package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "menu_item")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_menuitem")
public class MenuItem extends BaseEntity {

    private String name;
    @Column(name = "name_in_korean")
    private String nameInkorean;
    private String url;
}
