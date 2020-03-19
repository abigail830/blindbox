package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    Boolean isSpecial;
    Boolean isPresale;
    Long stock;
    BigDecimal probability;
    //Image f
    String productImage;
    MultipartFile productImageFile;

    String postCardImage;
    MultipartFile postCardImageFile;


}
