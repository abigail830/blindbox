package com.github.tuding.blindbox.domain.product;

import lombok.*;

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
    Long stock;
    //Image f
    String productImage;

    String productGrayImage;

    Long version;
}
