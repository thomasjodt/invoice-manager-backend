package org.jodt.service;

import org.jodt.entity.Payment;

import java.util.List;

public interface IPaymentService extends IService<Payment> {
    List<Payment> getPaymentsByInvoiceId(Long id);
}
