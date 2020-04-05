package com.github.tuding.blindbox.domain.user;

import lombok.*;

import java.util.List;
import java.util.Optional;

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

    String province;
    TransportFee transportFee;


    public ShippingAddress(long id, String receiver, String mobile, String area,
                           String associateCode, String detailAddress, Boolean isDefaultAddress, String province) {
        this.id = id;
        this.receiver = receiver;
        this.mobile = mobile;
        this.area = area;
        this.associateCode = associateCode;
        this.detailAddress = detailAddress;
        this.isDefaultAddress = isDefaultAddress;
        this.province = province;
    }

    public ShippingAddress(String receiver, String mobile, String area,
                           String associateCode, String detailAddress, Boolean isDefaultAddress, String province) {
        this.receiver = receiver;
        this.mobile = mobile;
        this.area = area;
        this.associateCode = associateCode;
        this.detailAddress = detailAddress;
        this.isDefaultAddress = isDefaultAddress;
        this.province = province;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setTransportFee(List<TransportFee> transportFeeList) {
        final Optional<TransportFee> transportFee = transportFeeList.stream()
                .filter(transportFee1 -> transportFee1.getArea().equals(this.province))
                .findFirst();
        transportFee.ifPresent(transportFee1 -> this.transportFee = transportFee1);
    }
}
