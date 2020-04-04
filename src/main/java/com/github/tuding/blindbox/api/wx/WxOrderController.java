package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.PlaceOrderRequest;
import com.github.tuding.blindbox.api.wx.wxDto.PlaceOrderResponse;
import com.github.tuding.blindbox.domain.order.OrderService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import com.github.tuding.blindbox.infrastructure.util.IpUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("wx/orders")
public class WxOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    IpUtil ipUtil;

    @Autowired
    Jwt jwt;

    @PostMapping("/product/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "抽盒后下单准备支付(需要带token) - under development")
    public PlaceOrderResponse placeOrder(HttpServletRequest request,
                                         @PathVariable String drawId,
                                         @RequestBody PlaceOrderRequest placeOrderRequest) {
        final String ipAddr = ipUtil.getIpAddr(request);

        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final String openId = jwt.getOpenIdFromToken(token);

        orderService.createOrder(openId, drawId, ipAddr, placeOrderRequest.getUseCoupon());
        return null;

    }
}
