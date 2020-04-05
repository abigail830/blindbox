package com.github.tuding.blindbox.domain;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.user.ShippingAddress;
import com.github.tuding.blindbox.domain.user.ShippingAddressService;
import com.github.tuding.blindbox.domain.user.User;
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
        assertEquals(1, shippingAddressService.getAddressWithTransportFeeByToken(token).size());
        assertEquals("openId1", shippingAddressService.getAddressWithTransportFeeByToken(token).get(0).getOpenId());
        assertEquals(Boolean.TRUE, shippingAddressService.getAddressWithTransportFeeByToken(token).get(0).getIsDefaultAddress());
    }

    @Test
    @DataSet(value = "test-data/save-1-address.yml")
    void addAddress_when_have_default() {
        //given
        final String token = jwt.generateWxToken(new User("openId", "skey"));
        final ShippingAddress shippingAddress = new ShippingAddress("receiver2", "12345678902",
                "area2", "associateCode2", "detailAddress2", Boolean.TRUE);
        //when
        shippingAddressService.addAddress(token, shippingAddress);

        final long count = shippingAddressService.getAddressWithTransportFeeByToken(token)
                .stream().filter(addr -> addr.getIsDefaultAddress() == Boolean.TRUE).count();
        assertEquals(1, count);
    }

    @Test
    @DataSet(value = "test-data/save-2-address.yml")
    @ExpectedDataSet("expect-data/save-address-openId3.yml")
    void deleteDefaultAddress() {
        //given
        final String token = jwt.generateWxToken(new User("openId3", "skey"));
        //when
        shippingAddressService.deleteAddress(token, 2);

        System.out.println(shippingAddressService.getAllAddress());
    }

    @Test
    @DataSet(value = "test-data/save-1-address.yml")
    void deleteDefaultAddress_when_its_the_only_1() {
        //given
        final String token = jwt.generateWxToken(new User("openId", "skey"));
        //when
        shippingAddressService.deleteAddress(token, 1);

        System.out.println(shippingAddressService.getAllAddress());
        assertEquals(0, shippingAddressService.getAddressWithTransportFeeByToken(token).size());
    }
}