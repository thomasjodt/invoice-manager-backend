package org.jodt.service;

import org.jodt.entity.Invoice;
import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;

import java.util.List;
import java.util.Optional;

public interface InvoiceIService extends IService<Invoice> {
    ResponseDTO<List<InvoiceDto>> getAllInvoices();
    ResponseDTO<List<InvoiceDto>> getAllInvoices(Integer limit, Integer offset);
    Optional<InvoiceDto> getInvoiceById(Long id);
    ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id);
}
