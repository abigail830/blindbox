package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@DBRider
public class OrderCreationTest {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    DrawRepository drawRepository;


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
                "sport"));

        //order
        orderRepository.save(new Order("testOrderID", "test",
                "d7493a65-1b22-479e-b3fe-decd80abe325", "test",
                BigDecimal.valueOf(100), "test", "test", "test", "test",
                "receiver", "12345678901", "area", "associateCode",
                "detailAddress", OrderStatus.NEW.name(), new Date()));

        assertThat(orderRepository.getAllOutstandingOrderByOpenId("test").get(0).getOrderId(), is("testOrderID"));

    }

}
