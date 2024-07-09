package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Invoice;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.InvoiceIRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceService implements InvoiceIService {
    @Inject
    InvoiceIRepository repository;

    @Override
    public ResponseDTO<List<Invoice>> getAll() {
        return repository.getAll();
    }

    @Override
    public ResponseDTO<List<Invoice>> getAll(Integer limit, Integer offset) {
        return repository.getAll(limit, offset);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public Invoice save(Invoice invoice) {
        invoice = repository.save(invoice);
        return invoice;
    }

    @Override
    public Invoice update(Invoice invoice) {
        invoice = repository.update(invoice);
        return invoice;
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public ResponseDTO<List<Invoice>> getInvoicesByVendorId(Long id) {
        return repository.getInvoicesByVendor(id);

    }

    @Override
    public ResponseDTO<List<Invoice>> getInvoicesByVendorId(Long id, Integer limit, Integer offset) {
        return repository.getInvoicesByVendor(id, limit, offset);
    }
}
