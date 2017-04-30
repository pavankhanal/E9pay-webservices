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
 * @since 4/28/2017
 */
@Entity
@Table(name = "push_notification_setting")
@Data
public class PushNotificationSetting extends BaseEntity {

    @Column(name = "enable_sound")
    private boolean enableSound;

    @Column(name = "message_on_locked_screen")
    private boolean messageOnLockedScreen;

    @Column(name = "amount_based_notice_enabled")
    private boolean amountBasedNoticeEnabled;

    @Column(name = "amount_from")
    private double amountFrom;

    @Column(name = "amount_to")
    private double amountTo;

    @Column(name = "notice_period_from")
    private LocalDate noticePeriodFrom;

    @Column(name = "notice_period_to")
    private LocalDate noticePeriodTo;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
