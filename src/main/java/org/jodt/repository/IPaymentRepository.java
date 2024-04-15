package org.jodt.repository;

import org.jodt.entity.Payment;

import java.util.List;

public interface IPaymentRepository extends IRepository<Payment> {
    List<Payment> getPaymentsByInvoiceId(Long id);
}
