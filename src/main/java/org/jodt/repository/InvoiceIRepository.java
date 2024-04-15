package org.jodt.repository;

import org.jodt.entity.Invoice;
import java.util.List;

public interface InvoiceIRepository  extends IRepository<Invoice> {
    List<Invoice> getInvoicesByVendor(Long vendorId);
}
