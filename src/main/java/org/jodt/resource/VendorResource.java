package org.jodt.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jodt.entity.Vendor;
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
    public List<VendorDto> getVendors(@QueryParam("name") String name, @QueryParam("page") Integer page, @QueryParam("offset") Integer offset) {

        if (name != null && !name.isBlank()) return service.findByName(name);
        if (page == null) return  service.getVendorsWithBalance();

        if (page == 0) page = 1;
        if(offset == null) offset = 5;
        return service.getVendorsWithBalance(offset, (page - 1) * offset);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public VendorDto getVendor(@PathParam("id") Long id) {
        Optional<VendorDto> vendor = Optional.ofNullable(service.getVendorWithBalanceById(id));
        return vendor.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VendorDto saveVendor(Vendor vendor) {
        vendor = service.save(vendor);
        return service.getVendorWithBalanceById(vendor.getId());
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VendorDto updateVendor(@PathParam("id") Long id, Vendor vendor) {
        var v = service.findById(id);
        if (v.isPresent()) {
            vendor.setId(id);
            vendor = service.update(vendor);
            return getVendor(vendor.getId());
        } else {
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteVendor(@PathParam("id") Long id) {
        var v = service.findById(id);

        if (v.isPresent()) {
            service.delete(id);
        }
    }
}
