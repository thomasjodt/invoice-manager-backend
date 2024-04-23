package org.jodt.repository;

import org.jodt.entity.Invoice;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface InvoiceIRepository  extends IRepository<Invoice> {
    ResponseDTO<List<Invoice>> getInvoicesByVendor(Long vendorId);
    ResponseDTO<List<Invoice>> getInvoicesByVendor(Long vendorId, Integer limit, Integer offset);
}
