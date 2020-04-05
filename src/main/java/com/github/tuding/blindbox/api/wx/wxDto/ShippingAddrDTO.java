package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.user.ShippingAddress;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddrDTO {

    Long id;
    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;
    Boolean isDefaultAddress;
    BigDecimal transportFee;

    public ShippingAddrDTO(ShippingAddress shippingAddress) {
        this.id = shippingAddress.getId();
        this.receiver = shippingAddress.getReceiver();
        this.mobile = shippingAddress.getMobile();
        this.area = shippingAddress.getArea();
        this.associateCode = shippingAddress.getAssociateCode();
        this.detailAddress = shippingAddress.getDetailAddress();
        this.isDefaultAddress = shippingAddress.getIsDefaultAddress();
        this.transportFee = shippingAddress.getTransportFee();
    }

    public ShippingAddress toDomainObjWithoutId() {
        return new ShippingAddress(receiver, mobile, area, associateCode, detailAddress, isDefaultAddress);
    }

    public ShippingAddress toDomainObjWithId() {
        return new ShippingAddress(id, receiver, mobile, area, associateCode, detailAddress, isDefaultAddress);
    }
}
