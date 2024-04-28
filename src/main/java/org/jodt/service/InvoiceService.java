package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;
import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.InvoiceIRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceService implements InvoiceIService {
    @Inject
    InvoiceIRepository repository;

    @Inject
    IPaymentService paymentService;

    @Override
    public ResponseDTO<List<InvoiceDto>> getAll() {
        ResponseDTO<List<Invoice>> invoices =  repository.getAll();
        return getListInvoiceDTO(invoices);
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getAll(Integer limit, Integer offset) {
        ResponseDTO<List<InvoiceDto>> invoices = this.getAll();
        if (limit > invoices.getCount() - offset) {
            limit = invoices.getCount() - offset;
        }
        invoices.setData(invoices.getData().subList(offset, offset + limit));

        if (limit > invoices.getCount() - offset) {
            limit = invoices.getCount() - offset;
        }
        invoices.setData(invoices.getData().subList(offset, offset + limit));
        return invoices;
    }

    @Override
    public Optional<InvoiceDto> findById(Long id) {
        Optional<Invoice>  o = Optional.ofNullable(repository.findById(id));
        InvoiceDto dto = null;

        if (o.isPresent()) {
            List<Payment> payments = paymentService.getPaymentsByInvoiceId(id).getData();
            dto = getInvoiceDto(o.get(), payments);
        }

        return Optional.ofNullable(dto);
    }

    @Override
    public InvoiceDto save(InvoiceDto invoice) {
        Invoice i = new Invoice(invoice);
        i = repository.save(i);
        Optional<InvoiceDto> dto = findById(i.getId());
        return dto.orElse(null);
    }

    @Override
    public InvoiceDto update(InvoiceDto invoice) {
        Invoice i = new Invoice(invoice);
        repository.update(i);
        Optional<InvoiceDto> dto = findById(i.getId());
        return dto.orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id) {
        ResponseDTO<List<Invoice>> res = repository.getInvoicesByVendor(id);
        return getListInvoiceDTO(res);
    }

    @Override
    public ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id, Integer limit, Integer offset) {
        ResponseDTO<List<InvoiceDto>> res = this.getInvoicesByVendorId(id);
        if (limit > res.getCount() - offset) {
            limit = res.getCount() - offset;
        }
        res.setData(res.getData().subList(offset, offset + limit));
        return res;
    }

    private static InvoiceDto getInvoiceDto(Invoice invoice, List<Payment> payments) {
        InvoiceDto dto = new InvoiceDto();

        dto.setId(invoice.getId());
        dto.setPayments(payments);
        dto.setAmount(invoice.getAmount());
        dto.setVendor(invoice.getVendor());
        dto.setDueDate(invoice.getDueDate());
        dto.setEmissionDate(invoice.getEmissionDate());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        return dto;
    }
    private ResponseDTO<List<InvoiceDto>> getListInvoiceDTO(ResponseDTO<List<Invoice>> invoices) {
        List<InvoiceDto> list = new ArrayList<>();

        invoices.getData().forEach((invoice) -> {
            List<Payment> payments = paymentService.getPaymentsByInvoiceId(invoice.getId()).getData();
            InvoiceDto invoiceDto = getInvoiceDto(invoice, payments);
            list.add(invoiceDto);
        });

        ResponseDTO<List<InvoiceDto>> dto = new ResponseDTO<>();
        dto.setData(list);
        dto.setCount(invoices.getCount());
        return dto;
    }
}
