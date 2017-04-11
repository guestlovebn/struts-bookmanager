/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author FIA
 */
@Entity
@Table(name = "loaisach")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loaisach.findAll", query = "SELECT l FROM Loaisach l")
    , @NamedQuery(name = "Loaisach.findByMaLoai", query = "SELECT l FROM Loaisach l WHERE l.maLoai = :maLoai")
    , @NamedQuery(name = "Loaisach.findByLoaiSach", query = "SELECT l FROM Loaisach l WHERE l.loaiSach = :loaiSach")})
public class Loaisach implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MaLoai")
    private String maLoai;
    @Size(max = 100)
    @Column(name = "LoaiSach")
    private String loaiSach;
    @OneToMany(mappedBy = "maLoai")
    private Collection<Sach> sachCollection;

    public Loaisach() {
    }

    public Loaisach(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getLoaiSach() {
        return loaiSach;
    }

    public void setLoaiSach(String loaiSach) {
        this.loaiSach = loaiSach;
    }

    @XmlTransient
    public Collection<Sach> getSachCollection() {
        return sachCollection;
    }

    public void setSachCollection(Collection<Sach> sachCollection) {
        this.sachCollection = sachCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maLoai != null ? maLoai.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Loaisach)) {
            return false;
        }
        Loaisach other = (Loaisach) object;
        if ((this.maLoai == null && other.maLoai != null) || (this.maLoai != null && !this.maLoai.equals(other.maLoai))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.asm.bookmanager.entity.Loaisach[ maLoai=" + maLoai + " ]";
    }
    
}
