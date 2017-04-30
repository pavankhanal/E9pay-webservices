package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "branch")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_branch")
public class Branch extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private Code country;

    @OneToOne
    @JoinColumn(name = "classified_code_id")
    private Code classified;

    private String name;

    @Column(name = "name_in_korean")
    private String nameInKorean;

    private String address;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String fax;

    @Column(name = "representative_name", length = 100)
    private String representativeName;

    @Column(name = "representative_phone", length = 50)
    private String representativePhone;

    @Column(name = "representative_email", length = 100)
    private String representativeEmail;
}
