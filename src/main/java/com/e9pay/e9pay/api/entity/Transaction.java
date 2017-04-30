package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 * @since 4/23/2017
 */
@Entity
@Table(name = "transaction")
@Data
public class Transaction extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "service_type_code_id")
    private Code serviceType;

    //TODO: may be replced by Code
    private String status;

    private double sentAmount;

    @Column(name = "e9pay_fee")
    private double e9payFee;

    private double recievedAmount;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "received_on")
    private DateTime receivedOn;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_status_updated_on")
    private DateTime lastStatusUpdatedOn;

    @OneToOne
    @JoinColumn(name = "withdrawal_information_id")
    WithdrawalInformation withdrawalInformation;
}
