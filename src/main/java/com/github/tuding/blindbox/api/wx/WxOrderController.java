package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.PayTransportReq;
import com.github.tuding.blindbox.api.wx.wxDto.PlaceOrderResponse;
import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderService;
import com.github.tuding.blindbox.domain.order.TransportOrder;
import com.github.tuding.blindbox.domain.user.ShippingAddressService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import com.github.tuding.blindbox.infrastructure.util.IpUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("wx/orders")
public class WxOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ShippingAddressService shippingAddressService;

    @Autowired
    IpUtil ipUtil;

    @Autowired
    Jwt jwt;

    @PostMapping("/product/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "抽盒后下单准备支付(需要带token) - pending wx test")
    public PlaceOrderResponse placeOrder(HttpServletRequest request,
                                         @PathVariable String drawId) {
        final String ipAddr = ipUtil.getIpAddr(request);

        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final String openId = jwt.getOpenIdFromToken(token);

        final Order order = orderService.createProductOrder(openId, drawId, ipAddr);
        return new PlaceOrderResponse(order);
    }

    @PostMapping("/transport")
    @NeedWxVerifyToken
    @ApiOperation(value = "支付运费(需要带token) - pending wx test")
    public PlaceOrderResponse payTransportFee(HttpServletRequest request, @RequestBody PayTransportReq payTransportReq) {

        final String ipAddr = ipUtil.getIpAddr(request);

        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final String openId = jwt.getOpenIdFromToken(token);

        BigDecimal transportFee = BigDecimal.ZERO;
        if (payTransportReq.getOrderIdList().size() < Constant.FREE_DELIVER) {
            transportFee = shippingAddressService.getTransportFeeByProvince(payTransportReq.getProvince());
        }
        final TransportOrder orders = payTransportReq.generateTransportOrder(openId, transportFee);

        final TransportOrder transportOrder = orderService.payTransportOrder(orders, ipAddr);
        return new PlaceOrderResponse(transportOrder);
    }
}
