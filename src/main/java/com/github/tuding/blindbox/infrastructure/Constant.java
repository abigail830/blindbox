package com.github.tuding.blindbox.infrastructure;

import java.math.BigDecimal;

public class Constant {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String ADMIN_UI_IMAGE_PATH = "/admin-ui/image/?path=";

    public static final String WX_UI_IMAGE_PATH = "/images";

    public static final int FIRST_LOGIN_BONUS = 5;

    public static final int GET_COUPON_CONSUME_BONUS = 2;
    public static final int GET_TIPS_COUPON_CONSUME_BONUS = 2;
    public static final int GET_DISPLAY_COUPON_CONSUME_BONUS = 5;
    public static final String COUPON_DESC = "这是您尊享的7折优惠券，仅需2点积分就可以换取啦！";
    public static final String TIPS_COUPON_DESC = "这是您尊享的提示卡，仅需2点积分就可以换取啦！";
    public static final String DISPLAY_COUPON_DESC = "这是您尊享的显示卡，仅需2点积分就可以换取啦！";
    public static final BigDecimal DISCOUNT = new BigDecimal("0.7");


    public static final String DRAW_INIT_STATUS = "NEW";
}
