package com.e9pay.e9pay.api.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "main_exchnage_rate_commission")
@Data
public class MainExchangeRateCommision extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private Code country;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "commision_seting_id")
    private List<CommisionSetting> commisionSettings;
}
