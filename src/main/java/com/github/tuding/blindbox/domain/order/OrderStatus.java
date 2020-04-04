package com.github.tuding.blindbox.domain.order;

public enum OrderStatus {

    NEW,
    PAY_PRODUCT_SUCCESS,
    PAY_PRODUCT_FAIL,
    PAY_PRODUCT_EXPIRY,

    NEW_TRANSPORT,
    PAY_TRANSPORT_SUCCESS,
    PAY_TRANSPORT_FAIL,
    PAY_TRANSPORT_EXPIRY,

    DELIVERED,
}
