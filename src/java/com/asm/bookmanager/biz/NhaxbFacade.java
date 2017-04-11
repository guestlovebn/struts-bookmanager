/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asm.bookmanager.biz;

import com.asm.bookmanager.entity.Nhaxb;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author FIA
 */
@Stateless
public class NhaxbFacade extends AbstractFacade<Nhaxb> {

    @PersistenceContext(unitName = "ASMStrutsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NhaxbFacade() {
        super(Nhaxb.class);
    }
    
}
