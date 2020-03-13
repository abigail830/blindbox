package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ProductDTO {
    Long id;
    Long seriesID;
    String name;
    boolean isSpecial;
    long stock;
    long probability;
    //Image f
    String productImage;
    String postCardImage;


}
