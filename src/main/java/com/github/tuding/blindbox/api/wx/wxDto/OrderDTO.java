package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.order.OrderWithProductInfo;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String orderId;
    private String openId;
    private String drawId;

    private String productName;
    private BigDecimal productPrice;

    private String receiver;
    private String mobile;
    private String area;
    private String associateCode;
    private String detailAddress;

    private String status;
    private Date createTime;

    private String shippingCompany;
    private String shippingTicket;
    private String productImage;

    public OrderDTO(OrderWithProductInfo order) {
        this.orderId = order.getOrderId();
        this.openId = order.getOpenId();
        this.drawId = order.getDrawId();
        this.productName = order.getProductName();
        this.productPrice = order.getProductPrice();
        this.receiver = order.getReceiver();
        this.mobile = order.getMobile();
        this.area = order.getArea();
        this.associateCode = order.getAssociateCode();
        this.detailAddress = order.getDetailAddress();
        this.status = order.getStatus();
        this.createTime = order.getCreateTime();
        this.shippingCompany = order.getShippingCompany();
        this.shippingTicket = order.getShippingTicket();
        this.productImage = Constant.WX_UI_IMAGE_PATH + order.getProductImage();
    }
}
