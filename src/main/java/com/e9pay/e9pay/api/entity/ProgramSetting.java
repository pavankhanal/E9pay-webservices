package com.e9pay.e9pay.api.entity;

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
@javax.persistence.Entity
@Table(name = "program_setting")
@Data
public class ProgramSetting extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "application_type_code_id")
    private Code applicationType;

    private String item;

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private Code country;

    private int setting;

    private String unit;

    @OneToOne
    @JoinColumn(name = "applies_to_service_code_id")
    private Code appliesToService;

    private LocalDate from;

    private LocalDate to;

    private String description;
}
