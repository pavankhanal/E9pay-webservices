package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/23/2017
 */
@Entity
@Table(name = "branch_exchange_commission_rate")
@Data
public class BranchExchangeCommissionRate extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private Code country;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "standard_trading_price")
    private double standardTradingPrice;

    @Column(name = "buy_price")
    private double buyPrice;

    private String description;

}
