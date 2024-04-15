package org.jodt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VendorDto {
    private Long id;
    private String name;
    private String fullName;
    private Double balance;
}
