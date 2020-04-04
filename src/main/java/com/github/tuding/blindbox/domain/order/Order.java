package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.infrastructure.client.payment.SignUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order {

    String orderId;
    String openId;
    String drawId;

    String productName;
    BigDecimal productPrice;

    String prepayId;
    String nonceStr;
    String preOrderTime;
    String paySign;

    String address;
    String receiver;
    String mobile;
    String area;
    String associateCode;
    String detailAddress;

    String status = "NEW";
    Date createTime;



    public Order(String orderId, String productName, BigDecimal productPrice, String openId, String drawId) {
        this.orderId = orderId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.openId = openId;
        this.drawId = drawId;
    }

    public void updateWxPayInfo(String prepayId, String nonceStr, String preOrderTime, String paySign) {
        this.prepayId = prepayId;
        this.nonceStr = nonceStr;
        this.preOrderTime = preOrderTime;
        this.paySign = paySign;
    }

    public void sign(String appid, String key) {
        String stringSignTemp = "appId=" + appid + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepayId +
                "&signType=MD5&timeStamp=" + preOrderTime;
        this.paySign = SignUtil.sign(stringSignTemp, key).toUpperCase();
    }
}
