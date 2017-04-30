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
@Table(name = "internationl_transaction")
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_international_transaction")
@Data
public class InternationalTransaction implements Identifiable<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hb_seq")
    private Long id = 0L;

    @Column(name = "transfer_fee")
    private double transferFee;

    @Column(name = "estimated_amount")
    private double estimatedAmount;

    @Column(name = "sent_amount")
    private double sentAmount;

    @Column(name = "main_commission_amount")
    private double mainCommissionAmount;

    @Column(name = "main_exchange_rate")
    private double mainExchangeRate;

    @Column(name = "branch_exchange_rate")
    private double branchExchangeRate;

    @OneToOne
    @JoinColumn(name = "receiver_country_code_id")
    private Code receiverCountry;

    @Column(name = "receiver_bank")
    private String receiverBank;

    @Column(name = "receiver_account_number")
    private long receiverAccountNumber;

    private String receiver;

    @Column(name = "bonus_amomunt")
    private double bonusAmount;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactionDetail;
}
