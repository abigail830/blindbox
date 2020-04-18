package com.github.tuding.blindbox.infrastructure.client.payment;

import com.github.tuding.blindbox.domain.order.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class WxPaymentRequestTest {


    @Test
    void testSign() {
        final Order order = new Order("T-mac all star 2", BigDecimal.ONE,
                "o4G6g4voOoZEkNeRuDDhVwf0p1IQ", "ed149e6c-36c8-48c8-9177-37f77a1a5f46");

        final WxPaymentRequest wxPaymentRequest = new WxPaymentRequest("wx6603c6d1c018bfd1", "1586641511",
                "Udol1029384756102938475610293847",
                "https://blindbox.fancier.store/wx/payment/product/callback",
                "0:0:0:0:0:0:0:1", order);
        final String result = wxPaymentRequest.convertToXmlWithSign();
        System.out.println(result);
    }
}