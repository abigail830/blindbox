package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ProductDTO {
    String id;
    String seriesID;
    String name;
    boolean isSpecial;
    boolean isPresale;
    long stock;
    BigDecimal probability;
    //Image f
    String productImage;
    String postCardImage;


}
