package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;
import org.jodt.repository.IPaymentRepository;
import org.jodt.repository.IRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PaymentService implements IPaymentService {
    @Inject
    IPaymentRepository repository;

    @Override
    public ResponseDTO<List<Payment>> getAll() {
        return repository.getAll();
    }

    @Override
    public ResponseDTO<List<Payment>> getAll(Integer page, Integer offset) {
        return repository.getAll(page, offset);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public Payment save(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        return repository.update(payment);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    public ResponseDTO<List<Payment>> getPaymentsByInvoiceId(Long id) {
        return repository.getPaymentsByInvoiceId(id);
    }
}
