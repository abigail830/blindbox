package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.user.ShippingAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DBRider
class ShippingAddressRepositoryTest {

    @Autowired
    ShippingAddressRepository shippingAddressRepository;

    @Test
    @DataSet("test-data/empty-address.yml")
    @ExpectedDataSet("expect-data/save-address.yml")
    void saveAddress() {
        //given
        final ShippingAddress shippingAddress = new ShippingAddress("receiver", "12345678901", "area",
                "associateCode", "detailAddress", Boolean.TRUE, "province");
        shippingAddress.setOpenId("openId");
        //when
        final ShippingAddress address = shippingAddressRepository.saveAddress(shippingAddress);
        //then
        assertEquals(1, shippingAddressRepository.getAllAddress().size());
        assertEquals(1, shippingAddressRepository.getAddressByOpenId("openId").size());
        assertEquals(1L, address.getId());

    }
}