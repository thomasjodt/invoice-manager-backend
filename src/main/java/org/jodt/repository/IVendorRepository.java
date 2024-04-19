package org.jodt.repository;

import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;

import java.util.List;

public interface IVendorRepository extends IRepository<Vendor> {
    ResponseDTO<List<VendorDto>> findByName(String name);
    ResponseDTO<List<VendorDto>> getVendorsWithBalance();
    ResponseDTO<List<VendorDto>> getVendorsWithBalance(Integer limit, Integer offset);
    VendorDto getVendorWithBalanceById(Long id);
}
