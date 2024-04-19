package org.jodt.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jodt.entity.Invoice;
import org.jodt.models.InvoiceDto;
import org.jodt.models.ResponseDTO;
import org.jodt.service.InvoiceIService;

import java.util.List;
import java.util.Optional;

@Path("/invoices")
public class InvoiceResource {
    @Inject
    private InvoiceIService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDTO<List<InvoiceDto>> getInvoices(@QueryParam("page") Integer page, @QueryParam("offset") Integer offset) {
        if (page == null) return  service.getAllInvoices();

        if (page == 0) page = 1;
        if (offset == null) offset = 5;
        return service.getAllInvoices(offset, (page - 1) * offset);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public InvoiceDto getInvoice(@PathParam("id") Long id) {
        Optional<InvoiceDto> invoice = service.getInvoiceById(id);
        return invoice.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Invoice saveInvoice(Invoice invoice) {
        invoice = service.save(invoice);

        var i = service.getInvoiceById(invoice.getId());
        return i.orElse(null);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Invoice updateInvoice(@PathParam("id") Long id, Invoice invoice) {
        var i = service.findById(id);
        if (i.isPresent()) {
            invoice.setId(id);
            invoice = service.update(invoice);
            System.out.println(invoice);
            return invoice;
        } else {
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteInvoice(@PathParam("id") Long id) {
        var i = service.findById(id);

        if (i.isPresent()) {
            service.delete(id);
        }
    }

    @GET
    @Path("/vendor/{vendorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDTO<List<InvoiceDto>> getInvoicesByVendor(@PathParam("vendorId") Long id) {
        return service.getInvoicesByVendorId(id);
    }
}
