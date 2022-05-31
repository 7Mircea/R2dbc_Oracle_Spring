package com.example.jpa_hikari_r2dbc_mvc.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("produse")
public class Produse {
    @Id
    @Column("id_prod")
    public Integer idProd; //NUMBER(10) in Oracle
    @Column("den_prod")
    public String denProd;
    @Column("id_furnizor")
    public int idFurnizor;
    @Column
    public String disponibilitate;
    @Column
    public String categorie;
    @Column("info_supl")
    public String infoSupl;
}
