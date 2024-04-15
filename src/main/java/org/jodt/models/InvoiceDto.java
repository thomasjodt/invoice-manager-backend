package org.jodt.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jodt.entity.Invoice;
import org.jodt.entity.Payment;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceDto extends Invoice {
    private List<Payment> payments;
}
