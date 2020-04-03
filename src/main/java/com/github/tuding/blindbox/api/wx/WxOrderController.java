package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.domain.order.OrderService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import com.github.tuding.blindbox.infrastructure.util.IpUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "下单准备支付(需要带token) - under development")
    public void placeOrder(HttpServletRequest request, @PathVariable String drawId) {
        final String ipAddr = ipUtil.getIpAddr(request);

        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final String openId = jwt.getOpenIdFromToken(token);

        orderService.createOrder(openId, drawId, ipAddr);

    }
}
