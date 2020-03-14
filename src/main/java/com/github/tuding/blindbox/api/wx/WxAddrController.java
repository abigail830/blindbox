package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.ShippingAddrDTO;
import com.github.tuding.blindbox.domain.ShippingAddress;
import com.github.tuding.blindbox.domain.ShippingAddressService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序相关登录和解密接口
 */
@RestController
@RequestMapping("/wx/address")
@Slf4j
public class WxAddrController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    @PostMapping
    @NeedWxVerifyToken
    public void addShippingAddress(HttpServletRequest request,
                                   @RequestBody ShippingAddrDTO shippingAddrDTO) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final ShippingAddress shippingAddress = shippingAddrDTO.toDomainObj();

        shippingAddressService.addAddress(token, shippingAddress);
    }

    @GetMapping
    @NeedWxVerifyToken
    public List<ShippingAddrDTO> getAddressByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return shippingAddressService.getAddressByToken(token).stream()
                .map(ShippingAddrDTO::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{addrId}")
    public void deleteAddress(HttpServletRequest request,
                              @PathVariable String addrId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        shippingAddressService.deleteAddress(token, Long.valueOf(addrId));
    }

}
