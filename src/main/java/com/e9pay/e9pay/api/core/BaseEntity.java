package com.e9pay.e9pay.api.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import lombok.Data;

import com.e9pay.e9pay.api.utils.DateConversionUtil;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class BaseEntity implements Cloneable, Serializable, Identifiable<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id = 0L;

    @Column(name = "creation_date", updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationDate = DateConversionUtil.getCurrentDateTimeSystem();

    @Column(name = "created_by_user_id", updatable = false)
    private Long createdByUserId;

    @Column(name = "update_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updateDate = DateConversionUtil.getCurrentDateTimeSystem();

    @Column(name = "updated_by_user_id")
    private Long updatedByUserId;
}

