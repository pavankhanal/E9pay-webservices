package com.e9pay.e9pay.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.Identifiable;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Entity
@Table(name = "authority_group_menu_item")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_authority_group_menu_item")
public class AuthorityGroupMenuItem implements Identifiable<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hb_seq")
    private Long id = 0L;

    @OneToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @Column(name="access_level")
    private int accessLevel;

}
