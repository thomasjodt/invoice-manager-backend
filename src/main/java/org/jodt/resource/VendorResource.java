package org.jodt.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jodt.entity.Vendor;
import org.jodt.models.ResponseDTO;
import org.jodt.service.IVendorService;

import java.util.List;
import java.util.Optional;

@Path("/vendors")
public class VendorResource {
    @Inject
    private IVendorService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDTO<List<Vendor>> getVendors(
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
    public Vendor getVendor(@PathParam("id") Long id) {
        Optional<Vendor> vendor = service.findById(id);
        return vendor.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vendor saveVendor(Vendor vendor) {
        vendor = service.save(vendor);
        return vendor;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Vendor updateVendor(@PathParam("id") Long id, Vendor vendor) {
        Optional<Vendor> v = service.findById(id);
        return v.orElse(null);
    }

    @DELETE
    @Path("/{id}")
    public void deleteVendor(@PathParam("id") Long id) {
        Optional<Vendor> v = service.findById(id);
        if (v.isPresent()) service.delete(id);

    }
}
