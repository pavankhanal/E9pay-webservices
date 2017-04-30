package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 * @since 4/24/2017
 */
@Entity
@Table(name = "withdrawal_information")
@Data
public class WithdrawalInformation extends BaseEntity {

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "account_number")
    private long accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "transfer_password")
    private String transferPassword;

    private boolean locked;

    @Column(name = "locked_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lockedOn;

    @Column(name = "invalid_attempt_count")
    private int invalidAttemptCount;

    @Column(name = "last_attempt_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastAttemptOn;

    @Column(name = "last_successfull_attempt_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastSuccessfullAttemptOn;

}
