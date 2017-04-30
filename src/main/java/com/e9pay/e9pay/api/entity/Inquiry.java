package com.e9pay.e9pay.api.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Entity
@Table(name = "inquiry")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_inquiry")
public class Inquiry extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String query;

    private String answer;

    private String status;

    @OneToOne
    @JoinColumn(name = "inquiry_type_code_id")
    private Code queryType;
}
