package com.e9pay.e9pay.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.Identifiable;
import com.e9pay.e9pay.api.utils.DateConversionUtil;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Entity
@Table(name = "exchange_rate")
@Data
public class ExchangeRate implements Cloneable, Serializable, Identifiable<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id = 0L;

    @Column(name = "currency_code", length = 20)
    private String currencyCode;

    @Column(name = "buy_cash_rate")
    private double buyCashRate;

    @Column(name = "sell_cash_rate")
    private double sellCashRate;

    @Column(name = "receive_telegraphic_transfer_rate")
    private double receiveTelegraphicTransferRate;

    @Column(name = "send_telegraphic_transfer_rate")
    private double sendTelegraphicTransferRate;

    @Column(name = "standard_trading_rate")
    private double standardTradingRate;

    @Column(name = "imported_from_file_name")
    private String importedFromFileName;

    @Column(name = "creation_date", updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn = DateConversionUtil.getCurrentDateTimeSystem();
}
