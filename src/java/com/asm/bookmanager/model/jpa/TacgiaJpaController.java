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
import com.asm.bookmanager.entity.Sach;
import com.asm.bookmanager.entity.Tacgia;
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
public class TacgiaJpaController implements Serializable {

    public TacgiaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tacgia tacgia) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tacgia.getSachCollection() == null) {
            tacgia.setSachCollection(new ArrayList<Sach>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Sach> attachedSachCollection = new ArrayList<Sach>();
            for (Sach sachCollectionSachToAttach : tacgia.getSachCollection()) {
                sachCollectionSachToAttach = em.getReference(sachCollectionSachToAttach.getClass(), sachCollectionSachToAttach.getMaSach());
                attachedSachCollection.add(sachCollectionSachToAttach);
            }
            tacgia.setSachCollection(attachedSachCollection);
            em.persist(tacgia);
            for (Sach sachCollectionSach : tacgia.getSachCollection()) {
                Tacgia oldMaTGOfSachCollectionSach = sachCollectionSach.getMaTG();
                sachCollectionSach.setMaTG(tacgia);
                sachCollectionSach = em.merge(sachCollectionSach);
                if (oldMaTGOfSachCollectionSach != null) {
                    oldMaTGOfSachCollectionSach.getSachCollection().remove(sachCollectionSach);
                    oldMaTGOfSachCollectionSach = em.merge(oldMaTGOfSachCollectionSach);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTacgia(tacgia.getMaTG()) != null) {
                throw new PreexistingEntityException("Tacgia " + tacgia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tacgia tacgia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tacgia persistentTacgia = em.find(Tacgia.class, tacgia.getMaTG());
            Collection<Sach> sachCollectionOld = persistentTacgia.getSachCollection();
            Collection<Sach> sachCollectionNew = tacgia.getSachCollection();
            Collection<Sach> attachedSachCollectionNew = new ArrayList<Sach>();
            for (Sach sachCollectionNewSachToAttach : sachCollectionNew) {
                sachCollectionNewSachToAttach = em.getReference(sachCollectionNewSachToAttach.getClass(), sachCollectionNewSachToAttach.getMaSach());
                attachedSachCollectionNew.add(sachCollectionNewSachToAttach);
            }
            sachCollectionNew = attachedSachCollectionNew;
            tacgia.setSachCollection(sachCollectionNew);
            tacgia = em.merge(tacgia);
            for (Sach sachCollectionOldSach : sachCollectionOld) {
                if (!sachCollectionNew.contains(sachCollectionOldSach)) {
                    sachCollectionOldSach.setMaTG(null);
                    sachCollectionOldSach = em.merge(sachCollectionOldSach);
                }
            }
            for (Sach sachCollectionNewSach : sachCollectionNew) {
                if (!sachCollectionOld.contains(sachCollectionNewSach)) {
                    Tacgia oldMaTGOfSachCollectionNewSach = sachCollectionNewSach.getMaTG();
                    sachCollectionNewSach.setMaTG(tacgia);
                    sachCollectionNewSach = em.merge(sachCollectionNewSach);
                    if (oldMaTGOfSachCollectionNewSach != null && !oldMaTGOfSachCollectionNewSach.equals(tacgia)) {
                        oldMaTGOfSachCollectionNewSach.getSachCollection().remove(sachCollectionNewSach);
                        oldMaTGOfSachCollectionNewSach = em.merge(oldMaTGOfSachCollectionNewSach);
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
                String id = tacgia.getMaTG();
                if (findTacgia(id) == null) {
                    throw new NonexistentEntityException("The tacgia with id " + id + " no longer exists.");
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
            Tacgia tacgia;
            try {
                tacgia = em.getReference(Tacgia.class, id);
                tacgia.getMaTG();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tacgia with id " + id + " no longer exists.", enfe);
            }
            Collection<Sach> sachCollection = tacgia.getSachCollection();
            for (Sach sachCollectionSach : sachCollection) {
                sachCollectionSach.setMaTG(null);
                sachCollectionSach = em.merge(sachCollectionSach);
            }
            em.remove(tacgia);
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

    public List<Tacgia> findTacgiaEntities() {
        return findTacgiaEntities(true, -1, -1);
    }

    public List<Tacgia> findTacgiaEntities(int maxResults, int firstResult) {
        return findTacgiaEntities(false, maxResults, firstResult);
    }

    private List<Tacgia> findTacgiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tacgia.class));
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

    public Tacgia findTacgia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tacgia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTacgiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tacgia> rt = cq.from(Tacgia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
