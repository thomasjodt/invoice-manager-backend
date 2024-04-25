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
    public ResponseDTO<List<InvoiceDto>> getInvoices(
        @QueryParam("page") Integer page,
        @QueryParam("offset") Integer offset,
        @QueryParam("vendorId") Long vendorId
    ) {
        if (page == null) {
            return (vendorId != null && vendorId > 0)
                ? service.getInvoicesByVendorId(vendorId)
                : service.getAll();
        }

        if (page == 0) page = 1;
        if (offset == null) offset = 5;

        return (vendorId != null && vendorId > 0)
            ? service.getInvoicesByVendorId(vendorId, offset, (page - 1) * offset)
            : service.getAll(offset, (page - 1) * offset);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public InvoiceDto getInvoice(@PathParam("id") Long id) {
        Optional<InvoiceDto> invoice = service.findById(id);
        return invoice.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Invoice saveInvoice(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto(invoice);
        return service.save(dto);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Invoice updateInvoice(@PathParam("id") Long id, Invoice invoice) {
        Optional<InvoiceDto> i = service.findById(id);

        if (i.isPresent()) {
            InvoiceDto dto = new InvoiceDto(invoice);
            return  service.update(dto);
        } else {
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteInvoice(@PathParam("id") Long id) {
        Optional<InvoiceDto> i = service.findById(id);
        if (i.isPresent()) service.delete(id);
    }
}
