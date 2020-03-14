package com.github.tuding.blindbox.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress implements Serializable {

    Long id;
    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;
    Boolean isDefaultAddress;
    String openId;


    public ShippingAddress(String receiver, String mobile, String area,
                           String associateCode, String detailAddress, Boolean isDefaultAddress) {
        this.receiver = receiver;
        this.mobile = mobile;
        this.area = area;
        this.associateCode = associateCode;
        this.detailAddress = detailAddress;
        this.isDefaultAddress = isDefaultAddress;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}
