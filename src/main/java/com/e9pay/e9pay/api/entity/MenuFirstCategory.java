package com.e9pay.e9pay.api.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "menu_first_category")
@Data
@SequenceGenerator(name = "hb_seq", sequenceName = "seq_menu_first_category")
public class MenuFirstCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_item_id")
    private MenuItem item;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_second_category_id")
    private List<MenuSecondCategory> secondCategories;

}
