package com.github.tuding.blindbox.domain.user;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    Long id;
    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;
    Boolean isDefaultAddress;
    String openId;


    public ShippingAddress(long id, String receiver, String mobile, String area,
                           String associateCode, String detailAddress, Boolean isDefaultAddress) {
        this.id = id;
        this.receiver = receiver;
        this.mobile = mobile;
        this.area = area;
        this.associateCode = associateCode;
        this.detailAddress = detailAddress;
        this.isDefaultAddress = isDefaultAddress;
    }

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
