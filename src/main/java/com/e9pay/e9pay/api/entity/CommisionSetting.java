package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Entity
@Table(name = "commision_setting")
@Data
public class CommisionSetting extends BaseEntity {

    @JoinColumn(name = "policy_code_id")
    private Code policy;

    @Column(length = 20)
    private String currency;

    @Column(name = "amount_range_from")
    private double amountRangeFrom;

    @Column(name = "amount_range_to")
    private double amountRangeTo;

    private double rate;

    @OneToOne
    @JoinColumn(name = "apply_currency_code_id")
    private Code applyCurrency;

    private String description;


}
