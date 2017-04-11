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
@Table(name = "tacgia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tacgia.findAll", query = "SELECT t FROM Tacgia t")
    , @NamedQuery(name = "Tacgia.findByMaTG", query = "SELECT t FROM Tacgia t WHERE t.maTG = :maTG")
    , @NamedQuery(name = "Tacgia.findByTenTG", query = "SELECT t FROM Tacgia t WHERE t.tenTG = :tenTG")
    , @NamedQuery(name = "Tacgia.findByDiaChi", query = "SELECT t FROM Tacgia t WHERE t.diaChi = :diaChi")
    , @NamedQuery(name = "Tacgia.findBySdt", query = "SELECT t FROM Tacgia t WHERE t.sdt = :sdt")
    , @NamedQuery(name = "Tacgia.findByEmail", query = "SELECT t FROM Tacgia t WHERE t.email = :email")})
public class Tacgia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MaTG")
    private String maTG;
    @Size(max = 100)
    @Column(name = "TenTG")
    private String tenTG;
    @Size(max = 500)
    @Column(name = "DiaChi")
    private String diaChi;
    @Size(max = 20)
    @Column(name = "SDT")
    private String sdt;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "Email")
    private String email;
    @OneToMany(mappedBy = "maTG")
    private Collection<Sach> sachCollection;

    public Tacgia() {
    }

    public Tacgia(String maTG) {
        this.maTG = maTG;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getTenTG() {
        return tenTG;
    }

    public void setTenTG(String tenTG) {
        this.tenTG = tenTG;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        hash += (maTG != null ? maTG.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tacgia)) {
            return false;
        }
        Tacgia other = (Tacgia) object;
        if ((this.maTG == null && other.maTG != null) || (this.maTG != null && !this.maTG.equals(other.maTG))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.asm.bookmanager.entity.Tacgia[ maTG=" + maTG + " ]";
    }
    
}
