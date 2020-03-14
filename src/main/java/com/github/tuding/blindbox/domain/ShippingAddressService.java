package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.repository.ShippingAddressRepository;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ShippingAddressService {

    @Autowired
    Jwt jwt;

    @Autowired
    ShippingAddressRepository shippingAddressRepository;

    @Transactional
    public void addAddress(String token, ShippingAddress shippingAddress) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        shippingAddress.setOpenId(openIdFromToken);
        final ShippingAddress addressSaved = shippingAddressRepository.saveAddress(shippingAddress);

        if (shippingAddress.getIsDefaultAddress()) {
            shippingAddressRepository.removeDefaultForOther(addressSaved);
        }

    }

    public List<ShippingAddress> getAllAddress() {
        return shippingAddressRepository.getAllAddress();
    }

    public List<ShippingAddress> getAddressByToken(String token) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        return shippingAddressRepository.getAddressByOpenId(openIdFromToken);
    }

    @Transactional
    public void deleteAddress(String token, String addrId) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        shippingAddressRepository.deleteAddressByOpenIdAndAddrId(openIdFromToken, addrId);
    }
}
