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
        TypedQuery<Vendor> q = em.createQuery("select v from Vendor v ORDER BY v.name ASC", Vendor.class);
        return getResponseDTO(q);
    }

    @Override
    public ResponseDTO<List<Vendor>> getAll(Integer limit, Integer offset) {
        TypedQuery<Vendor> q = em.createQuery("select v from Vendor v ORDER BY v.name ASC", Vendor.class);
        return getListResponseDTO(limit, offset, q);
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
        return getResponseDTO(q);
    }

    @Override
    public ResponseDTO<List<Vendor>> findByName(String name, Integer limit, Integer offset) {
        TypedQuery<Vendor> q  = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.fullName) LIKE LOWER(:vendorName) ORDER BY v.name ASC", Vendor.class);
        q.setParameter("vendorName", name);
        return getListResponseDTO(limit, offset, q);
    }

    private ResponseDTO<List<Vendor>> getListResponseDTO(Integer limit, Integer offset, TypedQuery<Vendor> q) {
        Integer count = getCount();

        q.setMaxResults(limit);
        q.setFirstResult(offset);
        List<Vendor> vendors = q.getResultList();

        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(vendors);

        return response;
    }
    private ResponseDTO<List<Vendor>> getResponseDTO(TypedQuery<Vendor> q) {
        List<Vendor> vendors = q.getResultList();
        Integer count = getCount();
        ResponseDTO<List<Vendor>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(vendors);

        return response;
    }

    @Override
    public Integer getCount() {
        return em.createQuery("SELECT COUNT(v) FROM Vendor v", Long.class).getSingleResult().intValue();
    }
}
