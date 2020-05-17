package com.github.tuding.blindbox.domain.order;

public enum OrderStatus {

    NEW("新产品订单"),
    PAY_PRODUCT_SUCCESS("产品已付款"),
    PAY_PRODUCT_FAIL("产品付款失败"),
    PAY_PRODUCT_EXPIRY("产品付款超时"),

    NEW_TRANSPORT("运费待付"),
    PAY_TRANSPORT_SUCCESS("运费已付"),
    PAY_TRANSPORT_FAIL("运费付款失败"),
    PAY_TRANSPORT_EXPIRY("运费付款超时"),

    DELIVERED("已发货");

    private String description;

    OrderStatus(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
