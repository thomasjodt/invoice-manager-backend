package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Invoice;

import java.util.List;

@ApplicationScoped
public class InvoicesRepository implements InvoiceIRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Override
    public List<Invoice> getAll() {
        return em.createQuery("select i from Invoice i", Invoice.class).getResultList();
    }

    @Override
    public List<Invoice> getAll(Integer limit, Integer offset) {
        var q = em.createQuery("select i from Invoice i", Invoice.class);
        q.setMaxResults(limit);
        q.setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    public Invoice findById(Long id) {
        return em.find(Invoice.class, id);
    }

    @Override
    public Invoice save(Invoice invoice) {
        try {
            transaction.begin();
            em.persist(invoice);
            transaction.commit();
            return invoice;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public Invoice update(Invoice invoice) {
        try {
            transaction.begin();
            em.merge(invoice);
            transaction.commit();
            return invoice;
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            transaction.begin();
            Invoice invoice = this.findById(id);
            em.remove(invoice);
            transaction.commit();
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Invoice> getInvoicesByVendor(Long vendorId) {
        TypedQuery<Invoice> q = em.createQuery("SELECT i FROM Invoice i WHERE i.vendor.id=:vendorId", Invoice.class);
        q.setParameter("vendorId", vendorId);
        return q.getResultList();
    }
}
