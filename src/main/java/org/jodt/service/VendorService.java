package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;
import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.IPaymentRepository;
import org.jodt.repository.IVendorRepository;
import org.jodt.repository.InvoiceIRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class VendorService implements IVendorService {
    @Inject
    private IVendorRepository repository;

    @Inject
    private IPaymentRepository paymentRepository;

    @Inject
    private InvoiceIRepository invoiceRepository;

    @Override
    public ResponseDTO<List<Vendor>> findByName(String vendorName) {
        return repository.findByName('%' + vendorName + '%');
    }

    @Override
    public ResponseDTO<List<Vendor>> findByName(String vendorName, Integer page, Integer offset) {
        return repository.findByName('%' + vendorName + '%', page, offset);
    }

    @Override
    public ResponseDTO<List<Vendor>> getAll() {
        var vendors = repository.getAll();
        List<Vendor> updated = new ArrayList<>();

        for (Vendor vendor : vendors.getData()) {
            ResponseDTO<List<Invoice>> invoices = invoiceRepository.getInvoicesByVendor(vendor.getId());
            List<Payment> payments = new ArrayList<>();

            Double balance = 0D;

            for (Invoice invoice : invoices.getData()) {
                AtomicReference<Double> totalAmount = new AtomicReference<>(0D);
                AtomicReference<Double> totalPaid = new AtomicReference<>(0D);

                ResponseDTO<List<Payment>> paymentsList = paymentRepository.getPaymentsByInvoiceId(invoice.getId());
                payments.addAll(paymentsList.getData());

                invoices.getData().forEach((i) -> totalAmount.updateAndGet(v -> v + i.getAmount()));
                payments.forEach((p) -> totalPaid.updateAndGet(v -> v + p.getAmount()));

                balance = totalAmount.get() - totalPaid.get();
            }

            vendor.setInvoices(invoices.getData());
            vendor.setPayments(payments);
            vendor.setBalance(balance);

            updated.add(vendor);
        }

        vendors.setData(updated);
        return vendors;
    }

    @Override
    public ResponseDTO<List<Vendor>> getAll(Integer page, Integer offset) {
        return repository.getAll(page, offset);
    }

    @Override
    public Optional<Vendor> findById(Long id) {
        Vendor vendor = repository.findById(id);
        return Optional.ofNullable(vendor);
    }

    @Override
    public Vendor save(Vendor vendor) {
        vendor = repository.save(vendor);
        return vendor;
    }

    @Override
    public Vendor update(Vendor vendor) {
        vendor = repository.update(vendor);
        return vendor;
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

}
