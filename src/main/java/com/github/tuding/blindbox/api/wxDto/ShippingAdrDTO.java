package com.github.tuding.blindbox.api.wxDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAdrDTO {

    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;
    Boolean isDefaultAddress;

}
