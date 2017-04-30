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
@Table(name = "domestic_transaction")
@SequenceGenerator(name = "hd_seq", sequenceName = "seq_domestic_transaction")
@Data
public class DomesticTransaction implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hd_seq")
    private Long id = 0L;

    @Column(name = "e9pay_fee")
    private double e9payFee;

    @Column(name = "receiver_bank")
    private String receiverBank;

    @Column(name = "receiver_account_number")
    private long receiverAccountNumber;

    private String receiver;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transactionDetail;
}
