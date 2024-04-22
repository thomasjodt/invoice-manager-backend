package org.jodt.repository;

import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;

import java.util.List;

public interface IVendorRepository extends IRepository<Vendor> {
    ResponseDTO<List<Vendor>> findByName(String name);
    ResponseDTO<List<Vendor>> findByName(String name, Integer limit, Integer offset);
}
