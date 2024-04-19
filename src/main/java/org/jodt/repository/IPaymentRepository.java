package org.jodt.repository;

import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface IPaymentRepository extends IRepository<Payment> {
    ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id);
}
