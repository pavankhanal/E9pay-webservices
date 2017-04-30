package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/23/2017
 */
@Entity
@Table(name = "beneficiary")
@Data
public class Beneficiary extends BaseEntity {

    private String name;

    private String address;

    private String phone;

    private String bankName;

    @Column(name = "account_number")
    private long accountNumber;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "citizenship_number")
    private String citizenshipNumber;

    @Column(name = "driver_license_number")
    private String driverLicenseNumber;



}
