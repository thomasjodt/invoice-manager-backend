package org.jodt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;
import org.jodt.repository.IVendorRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VendorService implements IVendorService {
    @Inject
    IVendorRepository repository;

    @Override
    public ResponseDTO<List<Vendor>> getAll() {
        return repository.getAll();
    }

    @Override
    public ResponseDTO<List<Vendor>> getAll(Integer page, Integer offset) {
        return repository.getAll(page, offset);
    }

    @Override
    public Optional<Vendor> findById(Long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public Vendor save(Vendor vendor) {
        return repository.save(vendor);
    }

    @Override
    public Vendor update(Vendor vendor) {
        return repository.update(vendor);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public ResponseDTO<List<VendorDto>> findByName(String vendorName) {
        return repository.findByName('%' + vendorName + '%');
    }

    @Override
    public ResponseDTO<List<VendorDto>> getVendorsWithBalance() {
        return repository.getVendorsWithBalance();
    }

    @Override
    public ResponseDTO<List<VendorDto>> getVendorsWithBalance(Integer limit, Integer offset) {
        return repository.getVendorsWithBalance(limit, offset);
    }

    @Override
    public VendorDto getVendorWithBalanceById(Long id) {
        return repository.getVendorWithBalanceById(id);
    }
}
