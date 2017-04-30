package com.e9pay.e9pay.api.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * @author Vivek Adhikari
 * @since 4/23/2017
 */
@Entity
@Table(name = "member")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_member")
public class Member extends BaseEntity {

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    private String nationality;

    @Column(name = "passport_number")
    @Lob
    private String passportNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "estimated_arrive_date")
    private LocalDate estimatedArriveDate;

    @Column(name = "social_security_number")
    @Lob
    private String socialSecurityNumber;

    @Column(name = "foreigner_registration_number")
    @Lob
    private String foreignerRegistrationNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @Column(name = "visa_issue_date")
    private LocalDate visaIssueDate;

    @Column(unique = true)
    @Lob
    private String email;

    private String password;

    private String mobile;

    @Column(name = "recommender_id")
    private String recommenderId;


    @Column(name = "invalid_login_count")
    private int invalidLoginCount;

    @Column(name = "last_login_attempt_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastLoginAttemptOn;

    @Column(name = "last_successfull_login_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastSuccessfullLoginOn;

    private boolean locked;

    @Column(name = "locked_on")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lockedOn;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "beneficiary_information_id")
    private List<Beneficiary> beneficiarys;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "withdrawal_information_id")
    private WithdrawalInformation withdrawalInformation;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    List<Transaction> transactions;

    @OneToOne
    @JoinColumn(name = "language_code_id")
    private Code language;

    @OneToOne
    @JoinColumn(name = "login_type_code_id")
    private Code loginType;

    @Lob
    @Column(name = "ocr_captured_indentification")
    private byte[] ocrCapturedIdentification;

    @Lob
    @Column(name = "facial_indentification")
    private byte[] facialIndentification;
}
