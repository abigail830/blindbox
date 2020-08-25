package com.github.tuding.blindbox.domain.wx.callback;

import com.github.tuding.blindbox.domain.order.Order;
import com.github.tuding.blindbox.domain.order.OrderStatus;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.repository.OrderRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WxCallbackApplService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    public void paySucess(String orderId) {

        log.info("Going to update order[{}] to {}", orderId, OrderStatus.PAY_PRODUCT_SUCCESS.name());
        Order order = orderRepository.updateOrderStatusAndQueryBack(orderId,
                OrderStatus.PAY_PRODUCT_SUCCESS.name());

        String openId = order.getOpenId();
        String drawId = order.getDrawId();
        List<OrderSimpleInfo> orderInfo = orderRepository.getOrderInfoByOpenIdAndDrawId(openId, drawId);

        if (orderInfo.isEmpty()) {
            log.info("No order found when searching by OpenId {} and drawId {}", openId, drawId);
        }

        String lastProductId = orderInfo.get(orderInfo.size() - 1).getProductId();
        if (lastProductBoughtBefore(orderInfo, lastProductId)) {
            updateBonusForBuyProduct(openId, orderId, Constant.BUY_PRODUCT);
        } else {
            if (isFullLightUp(orderInfo)) {
                updateBonusForBuyProduct(openId, orderId, Constant.LIGHT_UP_COLLECTION);
            } else {
                updateBonusForBuyProduct(openId, orderId, Constant.BUY_PRODUCT);
            }
        }
    }

    private Boolean isFullLightUp(List<OrderSimpleInfo> orderInfo) {
        List<String> productIdsBySeries = productRepository.getProductIdsBySeries(
                orderInfo.get(0).getSeriesId());
        List<String> boughtIds = orderInfo.stream()
                .map(OrderSimpleInfo::getProductId).collect(Collectors.toList());
        return boughtIds.containsAll(productIdsBySeries);
    }

    private boolean lastProductBoughtBefore(List<OrderSimpleInfo> orderInfo, String lastProductId) {
        return orderInfo.stream()
                .filter(orderSimpleInfo -> lastProductId.equals(orderSimpleInfo.getProductId()))
                .count() > 1;
    }

    private void updateBonusForBuyProduct(String openId, String orderId, Integer bonus) {
//Update user Bonus
        final int rowUpdated = userRepository.addBonus(openId, bonus);
        if (rowUpdated == 1) {
            log.info("{} bonus added for user {} purchase order {}", Constant.BUY_PRODUCT, openId, orderId);
        } else {
            log.warn("Fail to add bonus for order {}", orderId);
        }
    }
}
