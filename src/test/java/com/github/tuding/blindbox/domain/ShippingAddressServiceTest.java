package com.github.tuding.blindbox.domain;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@DBRider
class ShippingAddressServiceTest {

    @Autowired
    ShippingAddressService shippingAddressService;

    @Autowired
    Jwt jwt;

    @Test
    @DataSet(value = "test-data/empty-address.yml")
    void addAddress_when_no_other_default() {
        //given
        final String token = jwt.generateWxToken(new User("openId1", "skey"));
        final ShippingAddress shippingAddress = new ShippingAddress("receiver", "12345678901",
                "area", "associateCode", "detailAddress", Boolean.TRUE);
        //when
        shippingAddressService.addAddress(token, shippingAddress);
        //then
        assertEquals(1, shippingAddressService.getAddressByToken(token).size());
        assertEquals("openId1", shippingAddressService.getAddressByToken(token).get(0).getOpenId());
        assertEquals(Boolean.TRUE, shippingAddressService.getAddressByToken(token).get(0).getIsDefaultAddress());
    }

    @Test
    @DataSet(value = "test-data/save-1-address.yml")
    @ExpectedDataSet("expect-data/save-2-address.yml")
    void addAddress_when_have_default() {
        //given
        final String token = jwt.generateWxToken(new User("openId", "skey"));
        final ShippingAddress shippingAddress = new ShippingAddress("receiver2", "12345678902",
                "area2", "associateCode2", "detailAddress2", Boolean.TRUE);
        //when
        shippingAddressService.addAddress(token, shippingAddress);
        assertEquals(2, shippingAddressService.getAddressByToken(token).size());
    }
}