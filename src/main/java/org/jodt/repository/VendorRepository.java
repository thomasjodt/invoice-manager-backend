package org.jodt.repository;

import org.jodt.models.*;
import jakarta.persistence.*;
import org.jodt.entity.Vendor;
import jakarta.annotation.Resource;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class VendorRepository implements IVendorRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Override
    public ResponseDTO<List<Vendor>> getAll() {
        List<Vendor> result = em.createQuery("select v from Vendor v ORDER BY v.name ASC", Vendor.class).getResultList();

        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        Integer count = result.size();
        response.setCount(count);
        response.setData(result);

        return response;
    }

    @Override
    public ResponseDTO<List<Vendor>> getAll(Integer limit, Integer offset) {
        TypedQuery<Vendor> q = em.createQuery("select v from Vendor v ORDER BY v.name ASC", Vendor.class);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Vendor> vendors = q.getResultList();

        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(vendors);
        return response;
    }

    @Override
    public Vendor findById(Long id) {
        return em.find(Vendor.class, id);
    }

    @Override
    public Vendor save(Vendor vendor) {
        try {
            transaction.begin();
            em.persist(vendor);
            transaction.commit();
            return vendor;
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
    public Vendor update(Vendor vendor) {
        try {
            transaction.begin();
            em.merge(vendor);
            transaction.commit();
            return vendor;
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
            Vendor vendor = this.findById(id);
            em.remove(vendor);
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
    public ResponseDTO<List<Vendor>> findByName(String name) {
         TypedQuery<Vendor> q  = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.fullName) LIKE LOWER(:vendorName) ORDER BY v.name ASC", Vendor.class);
         q.setParameter("vendorName", name);

        List<Vendor> vendors = q.getResultList();
        Integer count = vendors.size();

        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(vendors);

        return  response;
    }

    @Override
    public ResponseDTO<List<Vendor>> findByName(String name, Integer limit, Integer offset) {
        TypedQuery<Vendor> q  = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.fullName) LIKE LOWER(:vendorName) ORDER BY v.name ASC", Vendor.class);
        q.setParameter("vendorName", name);
        Integer count = q.getResultList().size();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Vendor> vendors = q.getResultList();

        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(vendors);

        return  response;
    }
}
