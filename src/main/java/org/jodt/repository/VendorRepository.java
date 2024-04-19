package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Vendor;
import org.jodt.models.DebtDto;
import org.jodt.models.ResponseDTO;
import org.jodt.models.TotalPaymentsDto;
import org.jodt.models.VendorDto;

import java.util.ArrayList;
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

         Integer count = q.getResultList().size();

        List<Vendor> vendors = q.getResultList();
        List<VendorDto> list = new ArrayList<>();

        List<TotalPaymentsDto> payments = em.createQuery("SELECT i.vendor.id, SUM(p.amount) FROM Payment p LEFT JOIN Invoice i ON p.invoiceId = i.id GROUP BY p.invoiceId", TotalPaymentsDto.class).getResultList();
        List<DebtDto> debts = em.createQuery("SELECT i.vendor.id, SUM(i.amount) FROM Invoice i GROUP BY i.vendor.id", DebtDto.class).getResultList();


        vendors.forEach(i -> {
            var deudasOpt = debts.stream().filter(d -> d.getVendorId().equals(i.getId())).findFirst();
            var pagosOpt = payments.stream().filter(p -> p.getVendorId().equals(i.getId())).findFirst();

            VendorDto dto = new VendorDto(i.getId(), i.getName(), i.getFullName(), 0D);

            deudasOpt.ifPresent(total -> dto.setBalance(total.getDebt()));
            pagosOpt.ifPresent(totalPaymentsDto -> dto.setBalance(dto.getBalance() - totalPaymentsDto.getPayments()));
            list.add(dto);
        });

        ResponseDTO<List<VendorDto>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return  response;
    }

    @Override
    public ResponseDTO<List<VendorDto>> getVendorsWithBalance() {
        List<Vendor> vendors = em.createQuery("select v from Vendor v ORDER BY v.name ASC",Vendor.class).getResultList();

        Integer count = vendors.size();

        List<VendorDto> list = new ArrayList<>();

        List<TotalPaymentsDto> payments = em.createQuery("SELECT i.vendor.id, SUM(p.amount) FROM Payment p LEFT JOIN Invoice i ON p.invoiceId = i.id GROUP BY p.invoiceId", TotalPaymentsDto.class).getResultList();
        List<DebtDto> debts = em.createQuery("SELECT i.vendor.id, SUM(i.amount) FROM Invoice i GROUP BY i.vendor.id", DebtDto.class).getResultList();


        vendors.forEach(i -> {
            var deudasOpt = debts.stream().filter(d -> d.getVendorId().equals(i.getId())).findFirst();
            var pagosOpt = payments.stream().filter(p -> p.getVendorId().equals(i.getId())).findFirst();

            VendorDto dto = new VendorDto(i.getId(), i.getName(), i.getFullName(), 0D);

            deudasOpt.ifPresent(total -> dto.setBalance(total.getDebt()));
            pagosOpt.ifPresent(totalPaymentsDto -> dto.setBalance(dto.getBalance() - totalPaymentsDto.getPayments()));
            list.add(dto);
        });

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
        List<TotalPaymentsDto> payments = em.createQuery("SELECT i.vendor.id, SUM(p.amount) FROM Payment p LEFT JOIN Invoice i ON p.invoiceId = i.id GROUP BY p.invoiceId", TotalPaymentsDto.class).getResultList();
        List<DebtDto> debts = em.createQuery("SELECT i.vendor.id, SUM(i.amount) FROM Invoice i GROUP BY i.vendor.id", DebtDto.class).getResultList();


        vendors.forEach(i -> {
            var deudasOpt = debts.stream().filter(d -> d.getVendorId().equals(i.getId())).findFirst();
            var pagosOpt = payments.stream().filter(p -> p.getVendorId().equals(i.getId())).findFirst();

            VendorDto dto = new VendorDto(i.getId(), i.getName(), i.getFullName(), 0D);

            deudasOpt.ifPresent(total -> dto.setBalance(total.getDebt()));
            pagosOpt.ifPresent(totalPaymentsDto -> dto.setBalance(dto.getBalance() - totalPaymentsDto.getPayments()));
            list.add(dto);
        });

        ResponseDTO<List<VendorDto>> response = new ResponseDTO<>();
        response.setCount(count);
        response.setData(list);

        return  response;
    }

    @Override
    public VendorDto getVendorWithBalanceById(Long id) {
        Vendor vendor = this.findById(id);

        List<TotalPaymentsDto> payments = em.createQuery("SELECT i.vendor.id, SUM(p.amount) FROM Payment p LEFT JOIN Invoice i ON p.invoiceId = i.id GROUP BY p.invoiceId", TotalPaymentsDto.class).getResultList();
        List<DebtDto> debts = em.createQuery("SELECT i.vendor.id, SUM(i.amount) FROM Invoice i GROUP BY i.vendor.id", DebtDto.class).getResultList();

        var deudasOpt = debts.stream().filter(d -> d.getVendorId().equals(id)).findFirst();
        var pagosOpt = payments.stream().filter(p -> p.getVendorId().equals(id)).findFirst();

        VendorDto dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);

        deudasOpt.ifPresent(total -> dto.setBalance(total.getDebt()));
        pagosOpt.ifPresent(totalPaymentsDto -> dto.setBalance(dto.getBalance() - totalPaymentsDto.getPayments()));

        return dto;
    }
}
