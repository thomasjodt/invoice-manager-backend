package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Invoice;
import org.jodt.models.ResponseDTO;

import java.util.List;

@ApplicationScoped
public class InvoicesRepository implements InvoiceIRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Override
    public ResponseDTO<List<Invoice>> getAll() {
        List<Invoice> list = em.createQuery("select i from Invoice i ORDER BY i.emissionDate DESC", Invoice.class).getResultList();
        Integer count = list.size();

        ResponseDTO<List<Invoice>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return response;
    }

    @Override
    public ResponseDTO<List<Invoice>> getAll(Integer limit, Integer offset) {
        var q = em.createQuery("select i from Invoice i", Invoice.class);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Invoice> list = q.getResultList();

        ResponseDTO<List<Invoice>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return response;
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
    public ResponseDTO<List<Invoice>> getInvoicesByVendor(Long vendorId) {
        TypedQuery<Invoice> q = em.createQuery("SELECT i FROM Invoice i WHERE i.vendor.id=:vendorId", Invoice.class);
        q.setParameter("vendorId", vendorId);

        List<Invoice> list = q.getResultList();
        Integer count = list.size();

        ResponseDTO<List<Invoice>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return response;
    }
}
