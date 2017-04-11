/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.biz;

import com.asm.bookmanager.entity.Loaisach;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author FIA
 */
@Stateless
public class LoaisachFacade extends AbstractFacade<Loaisach> {

    @PersistenceContext(unitName = "ASMStrutsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoaisachFacade() {
        super(Loaisach.class);
    }
    
}
