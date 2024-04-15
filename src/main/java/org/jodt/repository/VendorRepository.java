package org.jodt.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.jodt.entity.Vendor;
import org.jodt.models.DebtDto;
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
    public List<Vendor> getAll() {
        return em.createQuery("select v from Vendor v", Vendor.class).getResultList();
    }

    @Override
    public List<Vendor> getAll(Integer limit, Integer offset) {
        var q = em.createQuery("select v from Vendor v", Vendor.class);
        q.setMaxResults(limit);
        q.setFirstResult(offset);
        return q.getResultList();
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
    public List<VendorDto> findByName(String name) {
        List<Vendor> vendors  = em.createQuery("SELECT v FROM Vendor v WHERE LOWER(v.fullName) LIKE LOWER(:vendorName)", Vendor.class)
            .setParameter("vendorName", name)
            .getResultList();

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

        return  list;
    }

    @Override
    public List<VendorDto> getVendorsWithBalance() {
        List<Vendor> vendors = em.createQuery("select v from Vendor v",Vendor.class).getResultList();
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

        return  list;
    }

    @Override
    public List<VendorDto> getVendorsWithBalance(Integer limit, Integer offset) {
        List<Vendor> vendors = em.createQuery("select v from Vendor v",Vendor.class)
            .setMaxResults(limit)
            .setFirstResult(offset)
            .getResultList();

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

        return  list;
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
