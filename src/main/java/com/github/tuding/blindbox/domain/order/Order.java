package com.github.tuding.blindbox.domain.order;

import com.github.tuding.blindbox.infrastructure.client.payment.SignUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order {

    protected String orderId;
    protected String openId;
    protected String drawId;

    protected String productName;
    protected BigDecimal productPrice;

    protected String prepayId;
    protected String nonceStr;
    protected String preOrderTime;
    protected String paySign;

    protected String receiver;
    protected String mobile;
    protected String area;
    protected String associateCode;
    protected String detailAddress;

    protected String status;
    protected Date createTime;
    protected Date updatedTime;

    protected String shippingCompany;
    protected String shippingTicket;

    public Order(String productName, BigDecimal productPrice, String openId, String drawId) {
        this.orderId = getRandomStringByLength(32);
        this.productName = productName;
        this.productPrice = productPrice;
        this.openId = openId;
        this.drawId = drawId;
        this.status = OrderStatus.NEW.name();
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

    protected String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
