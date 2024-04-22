package org.jodt.service;

import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;
import java.util.List;

public interface IVendorService extends IService<VendorDto> {
    ResponseDTO<List<VendorDto>> findByName(String vendorName);
    ResponseDTO<List<VendorDto>> findByName(String vendorName, Integer page, Integer offset);
}
