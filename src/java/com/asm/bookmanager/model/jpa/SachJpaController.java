/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.model.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.asm.bookmanager.entity.Loaisach;
import com.asm.bookmanager.entity.Tacgia;
import com.asm.bookmanager.entity.Nhaxb;
import com.asm.bookmanager.entity.Sach;
import com.asm.bookmanager.model.jpa.exceptions.NonexistentEntityException;
import com.asm.bookmanager.model.jpa.exceptions.PreexistingEntityException;
import com.asm.bookmanager.model.jpa.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author FIA
 */
public class SachJpaController implements Serializable {

    public SachJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sach sach) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Loaisach maLoai = sach.getMaLoai();
            if (maLoai != null) {
                maLoai = em.getReference(maLoai.getClass(), maLoai.getMaLoai());
                sach.setMaLoai(maLoai);
            }
            Tacgia maTG = sach.getMaTG();
            if (maTG != null) {
                maTG = em.getReference(maTG.getClass(), maTG.getMaTG());
                sach.setMaTG(maTG);
            }
            Nhaxb maNXB = sach.getMaNXB();
            if (maNXB != null) {
                maNXB = em.getReference(maNXB.getClass(), maNXB.getMaNXB());
                sach.setMaNXB(maNXB);
            }
            em.persist(sach);
            if (maLoai != null) {
                maLoai.getSachCollection().add(sach);
                maLoai = em.merge(maLoai);
            }
            if (maTG != null) {
                maTG.getSachCollection().add(sach);
                maTG = em.merge(maTG);
            }
            if (maNXB != null) {
                maNXB.getSachCollection().add(sach);
                maNXB = em.merge(maNXB);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSach(sach.getMaSach()) != null) {
                throw new PreexistingEntityException("Sach " + sach + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sach sach) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sach persistentSach = em.find(Sach.class, sach.getMaSach());
            Loaisach maLoaiOld = persistentSach.getMaLoai();
            Loaisach maLoaiNew = sach.getMaLoai();
            Tacgia maTGOld = persistentSach.getMaTG();
            Tacgia maTGNew = sach.getMaTG();
            Nhaxb maNXBOld = persistentSach.getMaNXB();
            Nhaxb maNXBNew = sach.getMaNXB();
            if (maLoaiNew != null) {
                maLoaiNew = em.getReference(maLoaiNew.getClass(), maLoaiNew.getMaLoai());
                sach.setMaLoai(maLoaiNew);
            }
            if (maTGNew != null) {
                maTGNew = em.getReference(maTGNew.getClass(), maTGNew.getMaTG());
                sach.setMaTG(maTGNew);
            }
            if (maNXBNew != null) {
                maNXBNew = em.getReference(maNXBNew.getClass(), maNXBNew.getMaNXB());
                sach.setMaNXB(maNXBNew);
            }
            sach = em.merge(sach);
            if (maLoaiOld != null && !maLoaiOld.equals(maLoaiNew)) {
                maLoaiOld.getSachCollection().remove(sach);
                maLoaiOld = em.merge(maLoaiOld);
            }
            if (maLoaiNew != null && !maLoaiNew.equals(maLoaiOld)) {
                maLoaiNew.getSachCollection().add(sach);
                maLoaiNew = em.merge(maLoaiNew);
            }
            if (maTGOld != null && !maTGOld.equals(maTGNew)) {
                maTGOld.getSachCollection().remove(sach);
                maTGOld = em.merge(maTGOld);
            }
            if (maTGNew != null && !maTGNew.equals(maTGOld)) {
                maTGNew.getSachCollection().add(sach);
                maTGNew = em.merge(maTGNew);
            }
            if (maNXBOld != null && !maNXBOld.equals(maNXBNew)) {
                maNXBOld.getSachCollection().remove(sach);
                maNXBOld = em.merge(maNXBOld);
            }
            if (maNXBNew != null && !maNXBNew.equals(maNXBOld)) {
                maNXBNew.getSachCollection().add(sach);
                maNXBNew = em.merge(maNXBNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = sach.getMaSach();
                if (findSach(id) == null) {
                    throw new NonexistentEntityException("The sach with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Sach sach;
            try {
                sach = em.getReference(Sach.class, id);
                sach.getMaSach();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sach with id " + id + " no longer exists.", enfe);
            }
            Loaisach maLoai = sach.getMaLoai();
            if (maLoai != null) {
                maLoai.getSachCollection().remove(sach);
                maLoai = em.merge(maLoai);
            }
            Tacgia maTG = sach.getMaTG();
            if (maTG != null) {
                maTG.getSachCollection().remove(sach);
                maTG = em.merge(maTG);
            }
            Nhaxb maNXB = sach.getMaNXB();
            if (maNXB != null) {
                maNXB.getSachCollection().remove(sach);
                maNXB = em.merge(maNXB);
            }
            em.remove(sach);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sach> findSachEntities() {
        return findSachEntities(true, -1, -1);
    }

    public List<Sach> findSachEntities(int maxResults, int firstResult) {
        return findSachEntities(false, maxResults, firstResult);
    }

    private List<Sach> findSachEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sach.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Sach findSach(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sach.class, id);
        } finally {
            em.close();
        }
    }

    public int getSachCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sach> rt = cq.from(Sach.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
