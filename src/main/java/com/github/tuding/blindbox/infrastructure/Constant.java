package com.github.tuding.blindbox.infrastructure;

import java.math.BigDecimal;

public class Constant {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String ADMIN_UI_IMAGE_PATH = "/admin-ui/image/?path=";

    public static final String WX_UI_IMAGE_PATH = "/images";

    public static final int FIRST_LOGIN_BONUS = 10;
    public static final int SHARE_COLLECTION = 15;
    public static final int LIGHT_UP_COLLECTION = 500;
    public static final int SHARE_ACTIVITY = 15;
    public static final int BUY_PRODUCT = 20;

    public static final int GET_DISCOUNT_COUPON_CONSUME_BONUS = 400;
    public static final int GET_TIPS_COUPON_CONSUME_BONUS = 100;
    public static final int GET_DISPLAY_COUPON_CONSUME_BONUS = 300;
    public static final String COUPON_DESC = "这是您尊享的7折优惠券，仅需2点积分就可以换取啦！";
    public static final String TIPS_COUPON_DESC = "这是您尊享的提示卡，仅需2点积分就可以换取啦！";
    public static final String DISPLAY_COUPON_DESC = "这是您尊享的显示卡，仅需2点积分就可以换取啦！";
    public static final BigDecimal DISCOUNT = new BigDecimal("0.7");


    public static final String DRAW_INIT_STATUS = "NEW";
    public static final String DRAW_ORDER_STATUS = "ORDER";
    public static final String DRAW_CANCELLED_STATUS = "CANCELLED";
    public static final int FREE_DELIVER = 3;
}
