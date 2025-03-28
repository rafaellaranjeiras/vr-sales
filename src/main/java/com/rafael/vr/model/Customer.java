package com.rafael.vr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private Integer code;
    private String name;
    private BigDecimal purchaseLimit;
    private Integer closingDay;

}
