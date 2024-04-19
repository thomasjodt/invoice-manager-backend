package org.jodt.service;

import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;

import java.util.List;


public interface IVendorService extends IService<Vendor> {
    ResponseDTO<List<VendorDto>> findByName(String vendorName);
    ResponseDTO<List<VendorDto>> getVendorsWithBalance();
    ResponseDTO<List<VendorDto>> getVendorsWithBalance(Integer limit, Integer offset);
    VendorDto getVendorWithBalanceById(Long id);

}
