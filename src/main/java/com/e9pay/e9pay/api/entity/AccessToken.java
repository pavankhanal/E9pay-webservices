package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.e9pay.e9pay.api.core.Identifiable;
import com.e9pay.e9pay.api.utils.DateConversionUtil;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * @author Vivek Adhikari
 * @since 4/28/2017
 */

@Entity
@Table(name = "access_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken implements Identifiable<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id = 0L;

    @Column(name = "creation_date", updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationDate = DateConversionUtil.getCurrentDateTimeSystem();

    private String token;

    private LocalDate expiry;


    public boolean isExpired() {
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @ManyToOne
    private User user;
}
