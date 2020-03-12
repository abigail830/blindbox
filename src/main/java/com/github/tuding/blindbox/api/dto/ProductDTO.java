package com.github.tuding.blindbox.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ProductDTO {
    String name;
    boolean isSpecial;
    //Image f
    String productImage;
    String postCardImage;
    long stock;
    long probability;

}
