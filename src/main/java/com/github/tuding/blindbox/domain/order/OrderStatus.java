package com.github.tuding.blindbox.domain.order;

public enum OrderStatus {

    CREATED,

    PAY_PRODUCT_SUCCESS,
    PAY_PRODUCT_FAIL,
    PAY_PRODUCT_EXPIRY,

    PAY_TRANSPORT_SUCCESS,
    PAY_TRANSPORT_FAIL,
    PAY_TRANSPORT_EXPIRY,

    DELIVERED,
}
