package org.jodt.service;

import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface InvoiceIService extends IService<InvoiceDto> {
    ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id);
    ResponseDTO<List<InvoiceDto>> getInvoicesByVendorId(Long id, Integer limit, Integer offset);
}
