package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.wx.callback.OrderSimpleInfo;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@DBRider
public class OrderCreationTest {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DrawRepository drawRepository;

    @Test
    @DataSet("test-data/get-order-simple-info.yml")
    public void getOrderInfoByOpenIdAndDrawId() {
        List<OrderSimpleInfo> result = orderRepository.getOrderInfoByOpenIdAndDrawId("test", "d7493a65-1b22-479e-b3fe-decd80abe326");
        System.out.println(result);
        assertEquals(result.size(), 2);
        assertEquals(result.stream().map(r -> r.getProductId())
                .filter(id -> id.equals("4340f163-f26d-4847-9d05-c33ff4134d76")).count(), 1);
        assertEquals(result.stream().map(r -> r.getProductId())
                .filter(id -> id.equals("4340f163-f26d-4847-9d05-c33ff4134d77")).count(), 1);
    }

    @Test
    @DataSet("test-data/empty-order.yml")
    void createOrder() throws ParseException {
        Toggle.TEST_MODE.setStatus(true);

        System.out.println(formatter.parse("2020-03-13"));
        //draw
        drawRepository.saveDraw(new Draw("test", "d7493a65-1b22-479e-b3fe-decd80abe325", "NEW",
                "4340f163-f26d-4847-9d05-c33ff4134d75", "f22be55f-dd01-456f-93c7-31e16618c83e",
                new Date(), BigDecimal.valueOf(100),
                "/series/f22be55f-dd01-456f-93c7-31e16618c83e-boxImage.png",
                false,
                "/product/f22be55f-dd01-456f-93c7-31e16618c83e-Image.png",
                "sport",
                null));

        //order
        orderRepository.save(new Order("testOrderID", "test",
                "d7493a65-1b22-479e-b3fe-decd80abe325", "test",
                BigDecimal.valueOf(100), "test", "test", "test", "test",
                "receiver", "12345678901", "area", "associateCode",
                "detailAddress", OrderStatus.NEW.name(), new Date(), null, null));

        assertThat(orderRepository.getAllOutstandingOrderByOpenId("test").get(0).getOrderId(), is("testOrderID"));

    }

}
