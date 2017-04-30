package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "code")
@Data
public class Code extends BaseEntity {

    @Column(name = "class_code", length = 15)
    private String classCode;

    @Column(name = "class_description")
    private String classDescription;

    @Column(length = 10)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(name = "name_in_korean")
    private String nameInKorean;
}
