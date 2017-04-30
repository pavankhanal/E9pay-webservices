package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;
import org.joda.time.LocalDate;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "transaction_setting")
@Data
public class TransactionSetting extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "classification_code_id")
    private Code classification;

    private String item;

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private Code country;

    @OneToOne
    @JoinColumn(name = "bank_code_id")
    private Code bank;

    @Column(name = "amount_range_from")
    private double amountRangeFrom;

    @Column(name = "amount_range_to")
    private double amountRangeTo;

    private double rate;

    @OneToOne
    @JoinColumn(name = "policy_code_id")
    private Code policy;

    @OneToOne
    @JoinColumn(name = "applieds_to_code_id")
    private Code appliesTo;

    @OneToOne
    @JoinColumn(name = "currency_code_id")
    private Code currency;

    private LocalDate from;

    private LocalDate to;

    private String description;
}
