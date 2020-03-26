package com.github.tuding.blindbox.domain;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Product {
    String id;
    String seriesID;
    String name;
    Boolean isSpecial;
    Boolean isPresale;
    Long stock;
    BigDecimal probability;
    //Image f
    String productImage;

    String productGrayImage;

    Long version;
}
