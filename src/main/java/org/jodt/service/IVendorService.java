package org.jodt.service;

import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface IVendorService extends IService<Vendor> {
    ResponseDTO<List<Vendor>> findByName(String vendorName);
    ResponseDTO<List<Vendor>> findByName(String vendorName, Integer page, Integer offset);
}
