package org.jodt.service;

import org.jodt.entity.Vendor;
import org.jodt.models.VendorDto;

import java.util.List;


public interface IVendorService extends IService<Vendor> {
    List<VendorDto> findByName(String vendorName);
    List<VendorDto> getVendorsWithBalance();
    List<VendorDto> getVendorsWithBalance(Integer limit, Integer offset);
    VendorDto getVendorWithBalanceById(Long id);

}
