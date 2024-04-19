package org.jodt.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jodt.entity.Payment;
import org.jodt.models.ResponseDTO;
import org.jodt.service.IPaymentService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Path("/payments")
public class PaymentResource {
    @Inject
    private IPaymentService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDTO<List<Payment>> getPayments(
        @QueryParam("vendor") Long id,
        @QueryParam("page") Integer page,
        @QueryParam("offset") Integer offset
    ) {
        if (id != null && id > 0) return service.getPaymentsByInvoiceId(id);
        if (page == null) return  service.getAll();

        if (page == 0) page = 1;
        if (offset == null) offset = 5;
        return service.getAll(offset, (page - 1) * offset);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPayment(@PathParam("id") Long id) {
        Optional<Payment> vendor = service.findById(id);
        return vendor.orElse(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Payment savePayment(Payment payment) {
        payment = service.save(payment);
        return payment;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Payment updatePayment(@PathParam("id") Long id, Payment payment) {
        Optional<Payment> v = service.findById(id);
        if (v.isPresent()) {
            payment.setId(id);
            payment = service.update(payment);
            return payment;
        } else {
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    public void deletePayment(@PathParam("id") Long id) {
        Optional<Payment> p = service.findById(id);
        if (p.isPresent()) service.delete(id);
    }
}
