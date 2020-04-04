package com.github.tuding.blindbox.domain.order;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TransportOrder extends Order {

    List<String> productOrders;

    public TransportOrder(List<String> productOrders, String openId,
                          String receiver, String mobile, String area, String associateCode, String detailAddress,
                          BigDecimal transportFee) {

        this.productOrders = productOrders;

        this.orderId = UUID.randomUUID().toString();
        this.openId = openId;
        this.receiver = receiver;
        this.mobile = mobile;
        this.area = area;
        this.associateCode = associateCode;
        this.detailAddress = detailAddress;
        this.status = OrderStatus.NEW_TRANSPORT.name();
        this.productName = "TRANSPORT_FEE";
        this.productPrice = transportFee;
    }

}
