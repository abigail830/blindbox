package com.github.tuding.blindbox.domain.user;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.repository.ShippingAddressRepository;
import com.github.tuding.blindbox.infrastructure.repository.TransportFeeRepository;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ShippingAddressService {

    @Autowired
    Jwt jwt;

    @Autowired
    ShippingAddressRepository shippingAddressRepository;

    @Autowired
    TransportFeeRepository transportFeeRepository;

    @Transactional
    public void addAddress(String token, ShippingAddress shippingAddress) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        shippingAddress.setOpenId(openIdFromToken);
        final ShippingAddress addressSaved = shippingAddressRepository.saveAddress(shippingAddress);

        if (shippingAddress.getIsDefaultAddress() && shippingAddress.getId() != null) {
            shippingAddressRepository.removeDefaultFlagForOther(addressSaved);
        }

    }

    public List<ShippingAddress> getAllAddress() {
        return shippingAddressRepository.getAllAddress();
    }

    public List<ShippingAddress> getAddressWithTransportFeeByToken(String token) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        final List<ShippingAddress> addresses = shippingAddressRepository.getAddressByOpenId(openIdFromToken);
        final List<TransportFee> transportFeeList = transportFeeRepository.getTransportFeeList();
        addresses.forEach(address -> address.setTransportFee(transportFeeList));
        return addresses;
    }

    @Transactional
    public void deleteAddress(String token, long addrId) {
        final String openId = jwt.getOpenIdFromToken(token);
        int rowUpdated = shippingAddressRepository.deleteAddressByOpenIdAndAddrId(openId, addrId);
        if (rowUpdated == 1) {
            shippingAddressRepository.updateLastAddrAsDefault(openId);
        } else {
            log.warn("No qualify address entry[id={}] for remove under user [{}]", addrId, openId);
            throw new BizException(ErrorCode.FAIL_TO_MODIFY_SHIPPING_ADDRESS);
        }
    }

    public void updateAddress(String token, ShippingAddress shippingAddress) {
        final String openIdFromToken = jwt.getOpenIdFromToken(token);
        shippingAddress.setOpenId(openIdFromToken);
        final int rowUpdated = shippingAddressRepository.updateAddress(shippingAddress);

        if (rowUpdated == 1 && shippingAddress.getIsDefaultAddress()) {
            shippingAddressRepository.removeDefaultFlagForOther(shippingAddress);
        } else {
            log.warn("No qualify address entry[id={}] for update under user [{}]",
                    shippingAddress.getId(), shippingAddress.getOpenId());
            throw new BizException(ErrorCode.FAIL_TO_MODIFY_SHIPPING_ADDRESS);
        }
    }

    public BigDecimal getTransportFeeByArea(String area) {
        final List<TransportFee> transportFeeList = transportFeeRepository.getTransportFeeList();
        final Optional<TransportFee> fee = transportFeeList.stream()
                .filter(transportFee -> area.equals(transportFee.getArea())).findFirst();
        if (fee.isPresent()) {
            return fee.get().getTransportFee();
        } else {
            throw new BizException(ErrorCode.TRANSPORT_AREA_NOT_SUPPORT);
        }
    }
}
