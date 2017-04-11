/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FIA
 */
@Entity
@Table(name = "sach")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sach.findAll", query = "SELECT s FROM Sach s")
    , @NamedQuery(name = "Sach.findByMaSach", query = "SELECT s FROM Sach s WHERE s.maSach = :maSach")
    , @NamedQuery(name = "Sach.findByTenSach", query = "SELECT s FROM Sach s WHERE s.tenSach = :tenSach")
    , @NamedQuery(name = "Sach.findByTomTat", query = "SELECT s FROM Sach s WHERE s.tomTat = :tomTat")})
public class Sach implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MaSach")
    private String maSach;
    @Size(max = 200)
    @Column(name = "TenSach")
    private String tenSach;
    @Size(max = 1000)
    @Column(name = "TomTat")
    private String tomTat;
    @JoinColumn(name = "MaLoai", referencedColumnName = "MaLoai")
    @ManyToOne
    private Loaisach maLoai;
    @JoinColumn(name = "MaTG", referencedColumnName = "MaTG")
    @ManyToOne
    private Tacgia maTG;
    @JoinColumn(name = "MaNXB", referencedColumnName = "MaNXB")
    @ManyToOne
    private Nhaxb maNXB;

    public Sach() {
    }

    public Sach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTomTat() {
        return tomTat;
    }

    public void setTomTat(String tomTat) {
        this.tomTat = tomTat;
    }

    public Loaisach getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(Loaisach maLoai) {
        this.maLoai = maLoai;
    }

    public Tacgia getMaTG() {
        return maTG;
    }

    public void setMaTG(Tacgia maTG) {
        this.maTG = maTG;
    }

    public Nhaxb getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(Nhaxb maNXB) {
        this.maNXB = maNXB;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maSach != null ? maSach.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sach)) {
            return false;
        }
        Sach other = (Sach) object;
        if ((this.maSach == null && other.maSach != null) || (this.maSach != null && !this.maSach.equals(other.maSach))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.asm.bookmanager.entity.Sach[ maSach=" + maSach + " ]";
    }
    
}
