package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class OrderDTO {

    protected String orderId;
    protected String openId;

    protected String productName;
    protected BigDecimal productPrice;

    protected String combineAddr;

    protected String status;
    protected Date createTime;

    protected String shippingCompany;
    protected String shippingTicket;

    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.openId = order.getOpenId();
        this.productName = order.getProductName();
        this.productPrice = order.getProductPrice();
        this.createTime = order.getCreateTime();
        this.shippingCompany = order.getShippingCompany();
        this.shippingTicket = order.getShippingTicket();
        this.combineAddr = buildAddress(order);
        this.status = remapStatus(order);
    }

    private String remapStatus(Order order) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equals(order.getStatus())) {
                return orderStatus.getDescription();
            }
        }
        return "Unknown";
    }

    private String buildAddress(Order order) {
        return order.getReceiver()
                + ", " + order.getMobile()
                + ", " + order.getArea()
                + ", " + order.getDetailAddress()
                + ", " + order.getAssociateCode();
    }
}
