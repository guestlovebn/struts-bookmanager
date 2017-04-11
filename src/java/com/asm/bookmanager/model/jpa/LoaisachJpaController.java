/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.model.jpa;

import com.asm.bookmanager.entity.Loaisach;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.asm.bookmanager.entity.Sach;
import com.asm.bookmanager.model.jpa.exceptions.NonexistentEntityException;
import com.asm.bookmanager.model.jpa.exceptions.PreexistingEntityException;
import com.asm.bookmanager.model.jpa.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author FIA
 */
public class LoaisachJpaController implements Serializable {

    public LoaisachJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Loaisach loaisach) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (loaisach.getSachCollection() == null) {
            loaisach.setSachCollection(new ArrayList<Sach>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Sach> attachedSachCollection = new ArrayList<Sach>();
            for (Sach sachCollectionSachToAttach : loaisach.getSachCollection()) {
                sachCollectionSachToAttach = em.getReference(sachCollectionSachToAttach.getClass(), sachCollectionSachToAttach.getMaSach());
                attachedSachCollection.add(sachCollectionSachToAttach);
            }
            loaisach.setSachCollection(attachedSachCollection);
            em.persist(loaisach);
            for (Sach sachCollectionSach : loaisach.getSachCollection()) {
                Loaisach oldMaLoaiOfSachCollectionSach = sachCollectionSach.getMaLoai();
                sachCollectionSach.setMaLoai(loaisach);
                sachCollectionSach = em.merge(sachCollectionSach);
                if (oldMaLoaiOfSachCollectionSach != null) {
                    oldMaLoaiOfSachCollectionSach.getSachCollection().remove(sachCollectionSach);
                    oldMaLoaiOfSachCollectionSach = em.merge(oldMaLoaiOfSachCollectionSach);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findLoaisach(loaisach.getMaLoai()) != null) {
                throw new PreexistingEntityException("Loaisach " + loaisach + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Loaisach loaisach) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Loaisach persistentLoaisach = em.find(Loaisach.class, loaisach.getMaLoai());
            Collection<Sach> sachCollectionOld = persistentLoaisach.getSachCollection();
            Collection<Sach> sachCollectionNew = loaisach.getSachCollection();
            Collection<Sach> attachedSachCollectionNew = new ArrayList<Sach>();
            for (Sach sachCollectionNewSachToAttach : sachCollectionNew) {
                sachCollectionNewSachToAttach = em.getReference(sachCollectionNewSachToAttach.getClass(), sachCollectionNewSachToAttach.getMaSach());
                attachedSachCollectionNew.add(sachCollectionNewSachToAttach);
            }
            sachCollectionNew = attachedSachCollectionNew;
            loaisach.setSachCollection(sachCollectionNew);
            loaisach = em.merge(loaisach);
            for (Sach sachCollectionOldSach : sachCollectionOld) {
                if (!sachCollectionNew.contains(sachCollectionOldSach)) {
                    sachCollectionOldSach.setMaLoai(null);
                    sachCollectionOldSach = em.merge(sachCollectionOldSach);
                }
            }
            for (Sach sachCollectionNewSach : sachCollectionNew) {
                if (!sachCollectionOld.contains(sachCollectionNewSach)) {
                    Loaisach oldMaLoaiOfSachCollectionNewSach = sachCollectionNewSach.getMaLoai();
                    sachCollectionNewSach.setMaLoai(loaisach);
                    sachCollectionNewSach = em.merge(sachCollectionNewSach);
                    if (oldMaLoaiOfSachCollectionNewSach != null && !oldMaLoaiOfSachCollectionNewSach.equals(loaisach)) {
                        oldMaLoaiOfSachCollectionNewSach.getSachCollection().remove(sachCollectionNewSach);
                        oldMaLoaiOfSachCollectionNewSach = em.merge(oldMaLoaiOfSachCollectionNewSach);
                    }
                }
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
                String id = loaisach.getMaLoai();
                if (findLoaisach(id) == null) {
                    throw new NonexistentEntityException("The loaisach with id " + id + " no longer exists.");
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
            Loaisach loaisach;
            try {
                loaisach = em.getReference(Loaisach.class, id);
                loaisach.getMaLoai();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loaisach with id " + id + " no longer exists.", enfe);
            }
            Collection<Sach> sachCollection = loaisach.getSachCollection();
            for (Sach sachCollectionSach : sachCollection) {
                sachCollectionSach.setMaLoai(null);
                sachCollectionSach = em.merge(sachCollectionSach);
            }
            em.remove(loaisach);
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

    public List<Loaisach> findLoaisachEntities() {
        return findLoaisachEntities(true, -1, -1);
    }

    public List<Loaisach> findLoaisachEntities(int maxResults, int firstResult) {
        return findLoaisachEntities(false, maxResults, firstResult);
    }

    private List<Loaisach> findLoaisachEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Loaisach.class));
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

    public Loaisach findLoaisach(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Loaisach.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoaisachCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Loaisach> rt = cq.from(Loaisach.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
