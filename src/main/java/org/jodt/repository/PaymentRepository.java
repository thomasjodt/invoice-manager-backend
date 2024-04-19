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
        TypedQuery<Payment> q =  em.createQuery("SELECT p FROM Payment p", Payment.class);
        var list = q.getResultList();
        Integer count = list.size();

        ResponseDTO<List<Payment>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);
        return response;
    }

    @Override
    public ResponseDTO<List<Payment>> getAll(Integer limit, Integer offset) {
        var q = em.createQuery("SELECT p FROM Payment p", Payment.class);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Payment> list = q.getResultList();

        ResponseDTO<List<Payment>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return response;
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

    public ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id) {
        TypedQuery<Payment> q = em.createQuery("select p from Payment p where p.invoiceId = ?1", Payment.class);
        q.setParameter(1,id);

        List<Payment> list = q.getResultList();
        Integer count = list.size();

        ResponseDTO<List<Payment>> response = new ResponseDTO<>();
        response.setData(list);
        response.setCount(count);

        return  response;
    }
}
