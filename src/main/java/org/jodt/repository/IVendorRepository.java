package org.jodt.repository;

import org.jodt.entity.Vendor;
import org.jodt.models.VendorDto;

import java.util.List;

public interface IVendorRepository extends IRepository<Vendor> {
    List<VendorDto> findByName(String name);
    List<VendorDto> getVendorsWithBalance();
    List<VendorDto> getVendorsWithBalance(Integer limit, Integer offset);
    VendorDto getVendorWithBalanceById(Long id);
}
