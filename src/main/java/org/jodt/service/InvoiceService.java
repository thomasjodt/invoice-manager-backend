package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.IPaymentRepository;
import org.jodt.repository.InvoiceIRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceService implements InvoiceIService {
    @Inject
    InvoiceIRepository repository;

    @Inject
    IPaymentRepository paymentRepository;

    @Override
    public ResponseDTO<List<Invoice>> getAll() {
        ResponseDTO<List<Invoice>> invoices =  repository.getAll();
        List<Invoice> updatedInvoices = updateInvoices(invoices);
        invoices.setData(updatedInvoices);
        return invoices;
    }

    @Override
    public ResponseDTO<List<Invoice>> getAll(Integer limit, Integer offset) {
        ResponseDTO<List<Invoice>> invoices = repository.getAll(limit, offset);
        List<Invoice> updatedInvoices = updateInvoices(invoices);
        invoices.setData(updatedInvoices);
        return invoices;
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
        ResponseDTO<List<Invoice>> invoices = repository.getInvoicesByVendor(id);
        List<Invoice> updatedInvoices = updateInvoices(invoices);
        invoices.setData(updatedInvoices);
        return invoices;

    }

    @Override
    public ResponseDTO<List<Invoice>> getInvoicesByVendorId(Long id, Integer limit, Integer offset) {
        ResponseDTO<List<Invoice>> invoices =  repository.getInvoicesByVendor(id, limit, offset);
        List<Invoice> updatedInvoices = updateInvoices(invoices);
        invoices.setData(updatedInvoices);
        return invoices;
    }

    private List<Invoice> updateInvoices(ResponseDTO<List<Invoice>> invoices) {
        List<Invoice> updatedInvoices = new ArrayList<>();
        for (Invoice invoice : invoices.getData()) {
            ResponseDTO<List<Payment>> payments = paymentRepository.getPaymentsByInvoiceId(invoice.getId());
            invoice.setPayments(payments.getData());
            updatedInvoices.add(invoice);
        }
        return updatedInvoices;
    }
}
