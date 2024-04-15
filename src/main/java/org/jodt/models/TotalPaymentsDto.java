package org.jodt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalPaymentsDto {
    private Long vendorId;
    private Double payments;
}
