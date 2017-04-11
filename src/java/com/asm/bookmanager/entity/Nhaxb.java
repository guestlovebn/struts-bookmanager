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
@Table(name = "nhaxb")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nhaxb.findAll", query = "SELECT n FROM Nhaxb n")
    , @NamedQuery(name = "Nhaxb.findByMaNXB", query = "SELECT n FROM Nhaxb n WHERE n.maNXB = :maNXB")
    , @NamedQuery(name = "Nhaxb.findByTenNXB", query = "SELECT n FROM Nhaxb n WHERE n.tenNXB = :tenNXB")
    , @NamedQuery(name = "Nhaxb.findByDiaChi", query = "SELECT n FROM Nhaxb n WHERE n.diaChi = :diaChi")
    , @NamedQuery(name = "Nhaxb.findBySdt", query = "SELECT n FROM Nhaxb n WHERE n.sdt = :sdt")
    , @NamedQuery(name = "Nhaxb.findByEmail", query = "SELECT n FROM Nhaxb n WHERE n.email = :email")})
public class Nhaxb implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MaNXB")
    private String maNXB;
    @Size(max = 100)
    @Column(name = "TenNXB")
    private String tenNXB;
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
    @OneToMany(mappedBy = "maNXB")
    private Collection<Sach> sachCollection;

    public Nhaxb() {
    }

    public Nhaxb(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
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
        hash += (maNXB != null ? maNXB.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nhaxb)) {
            return false;
        }
        Nhaxb other = (Nhaxb) object;
        if ((this.maNXB == null && other.maNXB != null) || (this.maNXB != null && !this.maNXB.equals(other.maNXB))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.asm.bookmanager.entity.Nhaxb[ maNXB=" + maNXB + " ]";
    }
    
}
