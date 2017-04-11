/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.model.jpa;

import com.asm.bookmanager.entity.Nhaxb;
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
public class NhaxbJpaController implements Serializable {

    public NhaxbJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nhaxb nhaxb) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (nhaxb.getSachCollection() == null) {
            nhaxb.setSachCollection(new ArrayList<Sach>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Sach> attachedSachCollection = new ArrayList<Sach>();
            for (Sach sachCollectionSachToAttach : nhaxb.getSachCollection()) {
                sachCollectionSachToAttach = em.getReference(sachCollectionSachToAttach.getClass(), sachCollectionSachToAttach.getMaSach());
                attachedSachCollection.add(sachCollectionSachToAttach);
            }
            nhaxb.setSachCollection(attachedSachCollection);
            em.persist(nhaxb);
            for (Sach sachCollectionSach : nhaxb.getSachCollection()) {
                Nhaxb oldMaNXBOfSachCollectionSach = sachCollectionSach.getMaNXB();
                sachCollectionSach.setMaNXB(nhaxb);
                sachCollectionSach = em.merge(sachCollectionSach);
                if (oldMaNXBOfSachCollectionSach != null) {
                    oldMaNXBOfSachCollectionSach.getSachCollection().remove(sachCollectionSach);
                    oldMaNXBOfSachCollectionSach = em.merge(oldMaNXBOfSachCollectionSach);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNhaxb(nhaxb.getMaNXB()) != null) {
                throw new PreexistingEntityException("Nhaxb " + nhaxb + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nhaxb nhaxb) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Nhaxb persistentNhaxb = em.find(Nhaxb.class, nhaxb.getMaNXB());
            Collection<Sach> sachCollectionOld = persistentNhaxb.getSachCollection();
            Collection<Sach> sachCollectionNew = nhaxb.getSachCollection();
            Collection<Sach> attachedSachCollectionNew = new ArrayList<Sach>();
            for (Sach sachCollectionNewSachToAttach : sachCollectionNew) {
                sachCollectionNewSachToAttach = em.getReference(sachCollectionNewSachToAttach.getClass(), sachCollectionNewSachToAttach.getMaSach());
                attachedSachCollectionNew.add(sachCollectionNewSachToAttach);
            }
            sachCollectionNew = attachedSachCollectionNew;
            nhaxb.setSachCollection(sachCollectionNew);
            nhaxb = em.merge(nhaxb);
            for (Sach sachCollectionOldSach : sachCollectionOld) {
                if (!sachCollectionNew.contains(sachCollectionOldSach)) {
                    sachCollectionOldSach.setMaNXB(null);
                    sachCollectionOldSach = em.merge(sachCollectionOldSach);
                }
            }
            for (Sach sachCollectionNewSach : sachCollectionNew) {
                if (!sachCollectionOld.contains(sachCollectionNewSach)) {
                    Nhaxb oldMaNXBOfSachCollectionNewSach = sachCollectionNewSach.getMaNXB();
                    sachCollectionNewSach.setMaNXB(nhaxb);
                    sachCollectionNewSach = em.merge(sachCollectionNewSach);
                    if (oldMaNXBOfSachCollectionNewSach != null && !oldMaNXBOfSachCollectionNewSach.equals(nhaxb)) {
                        oldMaNXBOfSachCollectionNewSach.getSachCollection().remove(sachCollectionNewSach);
                        oldMaNXBOfSachCollectionNewSach = em.merge(oldMaNXBOfSachCollectionNewSach);
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
                String id = nhaxb.getMaNXB();
                if (findNhaxb(id) == null) {
                    throw new NonexistentEntityException("The nhaxb with id " + id + " no longer exists.");
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
            Nhaxb nhaxb;
            try {
                nhaxb = em.getReference(Nhaxb.class, id);
                nhaxb.getMaNXB();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nhaxb with id " + id + " no longer exists.", enfe);
            }
            Collection<Sach> sachCollection = nhaxb.getSachCollection();
            for (Sach sachCollectionSach : sachCollection) {
                sachCollectionSach.setMaNXB(null);
                sachCollectionSach = em.merge(sachCollectionSach);
            }
            em.remove(nhaxb);
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

    public List<Nhaxb> findNhaxbEntities() {
        return findNhaxbEntities(true, -1, -1);
    }

    public List<Nhaxb> findNhaxbEntities(int maxResults, int firstResult) {
        return findNhaxbEntities(false, maxResults, firstResult);
    }

    private List<Nhaxb> findNhaxbEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nhaxb.class));
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

    public Nhaxb findNhaxb(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nhaxb.class, id);
        } finally {
            em.close();
        }
    }

    public int getNhaxbCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nhaxb> rt = cq.from(Nhaxb.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
