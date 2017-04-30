package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "notice")
@Data
public class Notice extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "notice_type_code_id")
    private Code noticeType;

    private String title;

    @Lob
    private String discription;

    @Column(name = "view_count")
    private int viewCount;

    private String attachment;

    private boolean urgent;
}
