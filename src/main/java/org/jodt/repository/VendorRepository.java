package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Vendor;
import org.jodt.models.*;
import org.jodt.service.InvoiceIService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class VendorRepository implements IVendorRepository {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction transaction;

    @Inject
    private InvoiceIService invoiceService;

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
        var q = em.createQuery("select v from Vendor v ORDER BY v.name ASC", Vendor.class);
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
    public ResponseDTO<List<VendorDto>> findByName(String name) {
         TypedQuery<Vendor> q  = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.fullName) LIKE LOWER(:vendorName) ORDER BY v.name ASC", Vendor.class);
         q.setParameter("vendorName", name);

        List<Vendor> vendors = q.getResultList();
        List<VendorDto> list = new ArrayList<>();
        Integer count = vendors.size();
        extracted(vendors, list);

        ResponseDTO<List<VendorDto>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return  response;
    }

    @Override
    public ResponseDTO<List<VendorDto>> getVendorsWithBalance() {
         var q = em.createQuery("select v from Vendor v ORDER BY v.name ASC",Vendor.class);

        List<Vendor> vendors = q.getResultList();
        List<VendorDto> list = new ArrayList<>();
        Integer count = vendors.size();
        extracted(vendors, list);

        ResponseDTO<List<VendorDto>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return  response;
    }

    @Override
    public ResponseDTO<List<VendorDto>> getVendorsWithBalance(Integer limit, Integer offset) {
         TypedQuery<Vendor> q = em.createQuery("select v from Vendor v ORDER BY v.name ASC",Vendor.class);
         Integer count = q.getResultList().size();

         q.setMaxResults(limit);
         q.setFirstResult(offset);

        List<Vendor> vendors = q.getResultList();
        List<VendorDto> list = new ArrayList<>();
        extracted(vendors, list);

        ResponseDTO<List<VendorDto>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return  response;
    }

    @Override
    public VendorDto getVendorWithBalanceById(Long id) {
        Vendor vendor = this.findById(id);
         ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(id);

        AtomicReference<Double> allPaid = new AtomicReference<>(0D);
        AtomicReference<Double> allAmount = new AtomicReference<>(0D);

        invoices.getData().forEach((i) -> {
            allAmount.updateAndGet(v -> v + i.getAmount());
            i.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
        });

        VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
        dto.setBalance(allAmount.get() - allPaid.get());

        return dto;
    }

    private void extracted(List<Vendor> vendors, List<VendorDto> list) {
        vendors.forEach(i -> {
            ResponseDTO<List<InvoiceDto>> invoices = invoiceService.getInvoicesByVendorId(i.getId());

            AtomicReference<Double> allPaid = new AtomicReference<>(0D);
            AtomicReference<Double> allAmount = new AtomicReference<>(0D);

            invoices.getData().forEach((invoice) -> {
                allAmount.updateAndGet(v -> v + invoice.getAmount());
                invoice.getPayments().forEach(p -> allPaid.updateAndGet(v -> v + p.getAmount()));
            });

            VendorDto dto = new VendorDto(i.getId(), i.getName(), i.getFullName(), 0D);
            dto.setBalance(allAmount.get() - allPaid.get());

            list.add(dto);
        });
    }
}
