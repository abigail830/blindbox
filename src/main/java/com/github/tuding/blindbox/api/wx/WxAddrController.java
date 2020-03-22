package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.ShippingAddrDTO;
import com.github.tuding.blindbox.domain.ShippingAddress;
import com.github.tuding.blindbox.domain.ShippingAddressService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "地址相关接口", description = "地址相关接口")
public class WxAddrController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    @PostMapping
    @NeedWxVerifyToken
    @ApiOperation(value = "添加发货地址(需要带token）")
    public void addShippingAddress(HttpServletRequest request,
                                   @RequestBody ShippingAddrDTO shippingAddrDTO) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final ShippingAddress shippingAddress = shippingAddrDTO.toDomainObjWithoutId();

        shippingAddressService.addAddress(token, shippingAddress);
    }

    @PutMapping
    @NeedWxVerifyToken
    @ApiOperation(value = "修改发货地址(需要带token）")
    public void updateShippingAddress(HttpServletRequest request,
                                      @RequestBody ShippingAddrDTO shippingAddrDTO) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final ShippingAddress shippingAddress = shippingAddrDTO.toDomainObjWithId();

        shippingAddressService.updateAddress(token, shippingAddress);
    }

    @GetMapping
    @NeedWxVerifyToken
    @ApiOperation(value = "根据token查询关联发货地址(需要带token）")
    public List<ShippingAddrDTO> getAddressByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return shippingAddressService.getAddressByToken(token).stream()
                .map(ShippingAddrDTO::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/id/{addrId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "删除发货地址(需要带token）")
    public void deleteAddress(HttpServletRequest request,
                              @PathVariable String addrId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        shippingAddressService.deleteAddress(token, Long.valueOf(addrId));
    }

}
