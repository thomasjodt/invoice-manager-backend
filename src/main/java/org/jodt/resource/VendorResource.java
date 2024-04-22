package org.jodt.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.models.VendorDto;
import org.jodt.service.IVendorService;

import java.util.List;
import java.util.Optional;

@Path("/vendors")
public class VendorResource {
    @Inject
    private IVendorService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDTO<List<VendorDto>> getVendors(
        @QueryParam("name") String name,
        @QueryParam("page") Integer page,
        @QueryParam("offset") Integer offset
    ) {
        if (page == null) {
            return (name != null && !name.isBlank())
                ? service.findByName(name)
                : service.getAll();
        }

        if (page == 0) page = 1;
        if (offset == null) offset = 5;

        return (name != null && !name.isBlank())
            ? service.findByName(name, offset, (page - 1) * offset)
            : service.getAll(offset, (page - 1) * offset);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public VendorDto getVendor(@PathParam("id") Long id) {
        Optional<VendorDto> vendor = service.findById(id);
        return vendor.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VendorDto saveVendor(Vendor vendor) {
        var dto = new VendorDto(vendor.getId(), vendor.getName(), vendor.getFullName(), 0D);
        dto = service.save(dto);
        return service.findById(dto.getId()).orElse(null);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VendorDto updateVendor(@PathParam("id") Long id, Vendor vendor) {
        Optional<VendorDto> v = service.findById(id);

        if (v.isPresent()) {
            VendorDto dto = v.get();
            dto.setId(id);
            dto.setName(vendor.getName());
            dto.setFullName(vendor.getFullName());
            return service.update(dto);
        } else {
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteVendor(@PathParam("id") Long id) {
        Optional<VendorDto> v = service.findById(id);

        if (v.isPresent()) {
            service.delete(id);
        }
    }
}
