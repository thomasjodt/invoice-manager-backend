package org.jodt.service;

import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface IPaymentService extends IService<Payment> {
    ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id);
    ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id, Integer limit, Integer offset);
}
