package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.order.TransportOrder;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PayTransportReq {

    List<String> orderIdList;

    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;

    public TransportOrder toTransportOrder(String openId, BigDecimal transportFee) {
        return new TransportOrder(orderIdList, openId, receiver, mobile, area, associateCode, detailAddress, transportFee);
    }

}
