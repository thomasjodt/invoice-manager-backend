package org.jodt.service;

import org.jodt.entity.Invoice;
import org.jodt.models.InvoiceDto;

import java.util.List;
import java.util.Optional;

public interface InvoiceIService extends IService<Invoice> {
    List<InvoiceDto> getAllInvoices();
    List<InvoiceDto> getAllInvoices(Integer limit, Integer offset);
    Optional<InvoiceDto> getInvoiceById(Long id);
    List<InvoiceDto> getInvoicesByVendorId(Long id);
}
