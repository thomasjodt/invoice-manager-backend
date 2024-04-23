package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;

import java.util.List;

@ApplicationScoped
public class PaymentRepository implements IPaymentRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Override
    public ResponseDTO<List<Payment>> getAll() {
        TypedQuery<Payment> q = em.createQuery("SELECT p FROM Payment p ORDER BY p.paymentDate", Payment.class);
        List<Payment> payments = q.getResultList();
        Integer count = payments.size();

        ResponseDTO<List<Payment>> dto = new ResponseDTO<>();
        dto.setCount(count);
        dto.setData(payments);

        return dto;
    }

    @Override
    public ResponseDTO<List<Payment>> getAll(Integer limit, Integer offset) {
        TypedQuery<Payment> q = em.createQuery("SELECT p FROM Payment p ORDER BY p.paymentDate", Payment.class);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Payment> payments = q.getResultList();

        ResponseDTO<List<Payment>> dto = new ResponseDTO<>();
        dto.setCount(count);
        dto.setData(payments);

        return dto;
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

    @Override
    public ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id) {
        TypedQuery<Payment> q = em.createQuery("SELECT p FROM Payment p WHERE p.invoiceId = :id ORDER BY p.paymentDate", Payment.class);
        q.setParameter("id", id);

        List<Payment> payments = q.getResultList();
        Integer count = payments.size();

        ResponseDTO<List<Payment>> dto = new ResponseDTO<>();
        dto.setCount(count);
        dto.setData(payments);

        return dto;
    }

    @Override
    public ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id, Integer limit, Integer offset) {
        TypedQuery<Payment> q = em.createQuery("SELECT p FROM Payment p WHERE p.invoiceId = :id ORDER BY p.paymentDate", Payment.class);
        q.setParameter("id", id);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Payment> payments = q.getResultList();

        ResponseDTO<List<Payment>> dto = new ResponseDTO<>();
        dto.setCount(count);
        dto.setData(payments);

        return dto;
    }
}
