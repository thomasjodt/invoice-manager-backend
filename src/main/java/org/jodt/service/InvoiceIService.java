package org.jodt.service;

import org.jodt.entity.Invoice;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface InvoiceIService extends IService<Invoice> {
    ResponseDTO<List<Invoice>> getInvoicesByVendorId(Long id);
    ResponseDTO<List<Invoice>> getInvoicesByVendorId(Long id, Integer limit, Integer offset);
}
