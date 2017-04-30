package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.Identifiable;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Entity
@Table(name = "recharge_transaction")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_recharge_transaction")
public class RechargeTransaction implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hb_seq")
    private Long id = 0L;

    @Column(name = "bonus_amount")
    private double bonusAmount;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactionDetail;
}
