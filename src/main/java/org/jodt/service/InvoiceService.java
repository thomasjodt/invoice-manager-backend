package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;
import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.IPaymentRepository;
import org.jodt.repository.InvoiceIRepository;

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
        return repository.getAll();
    }

    @Override
    public ResponseDTO<List<Invoice>> getAll(Integer limit, Integer offset) {
        return repository.getAll(limit,offset);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public Invoice update(Invoice invoice) {
        return repository.update(invoice);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getAllInvoices() {
        ResponseDTO<List<Invoice>> res = this.getAll();
        List<Invoice> invoiceList = res.getData();

        var list = invoiceList.stream().map(i -> {
            List<Payment> payments = paymentRepository.getPaymentsByInvoiceId(i.getId()).getData();
            return getInvoiceDto(i, payments);
        }).toList();

        ResponseDTO<List<InvoiceDto>> response = new ResponseDTO<>();
        response.setCount(res.getCount());
        response.setData(list);

        return response;
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getAllInvoices(Integer limit, Integer offset) {
        ResponseDTO<List<Invoice>> res = this.getAll(limit, offset);
        List<Invoice> invoiceList = res.getData();

        List<InvoiceDto> list = invoiceList.stream().map(i -> {
            List<Payment> payments = paymentRepository.getPaymentsByInvoiceId(i.getId()).getData();
            return getInvoiceDto(i, payments);
        }).toList();

        ResponseDTO<List<InvoiceDto>> response = new ResponseDTO<>();
        response.setCount(res.getCount());
        response.setData(list);

        return response;
    }

    @Override
    public Optional<InvoiceDto> getInvoiceById(Long id) {
        Optional<Invoice> i = this.findById(id);
        InvoiceDto dto = null;

        if (i.isPresent()) {
            Invoice invoice = i.get();
            List<Payment> payments = paymentRepository.getPaymentsByInvoiceId(invoice.getId()).getData();
            dto = getInvoiceDto(invoice, payments);
        }
        return Optional.ofNullable(dto);
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id) {
        var res = repository.getInvoicesByVendor(id);
        var invoiceList = res.getData();

        var list = invoiceList.stream().map(i -> {
            List<Payment> payments = paymentRepository.getPaymentsByInvoiceId(i.getId()).getData();
            return getInvoiceDto(i, payments);
        }).toList();

        ResponseDTO<List<InvoiceDto>> response = new ResponseDTO<>();
        response.setCount(res.getCount());
        response.setData(list);

        return response;
    }

    private static InvoiceDto getInvoiceDto(Invoice invoice, List<Payment> payments) {
        InvoiceDto dto;
        dto = new InvoiceDto();

        dto.setId(invoice.getId());
        dto.setPayments(payments);
        dto.setAmount(invoice.getAmount());
        dto.setVendor(invoice.getVendor());
        dto.setDueDate(invoice.getDueDate());
        dto.setEmissionDate(invoice.getEmissionDate());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        return dto;
    }
}
