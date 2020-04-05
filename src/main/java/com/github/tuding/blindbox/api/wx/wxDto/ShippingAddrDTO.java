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

    private static final int NOT_SUPPORT = -2;
    private static final int U_PAY_WHEN_ARRIVED = -1;
    Long id;
    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;
    Boolean isDefaultAddress;
    BigDecimal transportFee;
    String province;

    public ShippingAddrDTO(ShippingAddress shippingAddress) {
        this.id = shippingAddress.getId();
        this.receiver = shippingAddress.getReceiver();
        this.mobile = shippingAddress.getMobile();
        this.area = shippingAddress.getArea();
        this.associateCode = shippingAddress.getAssociateCode();
        this.detailAddress = shippingAddress.getDetailAddress();
        this.isDefaultAddress = shippingAddress.getIsDefaultAddress();
        this.province = shippingAddress.getProvince();

        updateTransportFee(shippingAddress);
    }

    private void updateTransportFee(ShippingAddress shippingAddress) {
        if (null == shippingAddress.getTransportFee()) {
            this.transportFee = BigDecimal.valueOf(NOT_SUPPORT);
        } else if (shippingAddress.getTransportFee().getUPay()) {
            this.transportFee = BigDecimal.valueOf(U_PAY_WHEN_ARRIVED);
        } else {
            this.transportFee = shippingAddress.getTransportFee().getTransportFee();
        }
    }

    public ShippingAddress toDomainObjWithoutId() {
        return new ShippingAddress(receiver, mobile, area, associateCode, detailAddress, isDefaultAddress, province);
    }

    public ShippingAddress toDomainObjWithId() {
        return new ShippingAddress(id, receiver, mobile, area, associateCode, detailAddress, isDefaultAddress, province);
    }
}
