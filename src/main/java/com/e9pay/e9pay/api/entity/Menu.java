package com.e9pay.e9pay.api.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

import com.e9pay.e9pay.api.core.BaseEntity;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Entity
@Table(name = "member")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_menu")
public class Menu extends BaseEntity {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_first_category_id")
    List<MenuFirstCategory> firstCategories;

}
