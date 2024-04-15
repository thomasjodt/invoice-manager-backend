package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Payment;

import java.util.List;

@ApplicationScoped
public class PaymentRepository implements IPaymentRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Override
    public List<Payment> getAll() {
        return em.createQuery("SELECT p FROM Payment p", Payment.class).getResultList();
    }

    @Override
    public List<Payment> getAll(Integer limit, Integer offset) {
        var q = em.createQuery("SELECT p FROM Payment p", Payment.class);
        q.setMaxResults(limit);
        q.setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }

    @Override
    public Payment save(Payment payment) {
        try {
            transaction.begin();
            em.persist(payment);
            transaction.commit();
            return payment;
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
    public Payment update(Payment payment) {
        try {
            transaction.begin();
            em.merge(payment);
            transaction.commit();
            return payment;
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
            Payment payment = this.findById(id);
            em.remove(payment);
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

    public List<Payment> getPaymentsByInvoiceId(Long id) {
        return em.createQuery("select p from Payment p where p.invoiceId = ?1", Payment.class)
            .setParameter(1,id)
            .getResultList();
    }
}
