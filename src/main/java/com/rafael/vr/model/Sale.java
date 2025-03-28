package com.rafael.vr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Sale {

    private Integer code;
    private Integer customerCode;
    private BigDecimal totalPrice;
    private List<SaleItem> items;

    @Data
    @AllArgsConstructor
    public static class SaleItem {
        private Integer productCode;
        private String productDescription;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

}
