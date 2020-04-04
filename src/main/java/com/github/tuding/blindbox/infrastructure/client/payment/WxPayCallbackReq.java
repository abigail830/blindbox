package com.github.tuding.blindbox.infrastructure.client.payment;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallbackReq {

    private String return_code;
    private String return_msg;

    private String result_code;
    private String appid;
    private String mch_id;
    private String sign;
    private String nonce_str;
    private String openid;
    private Boolean is_subscribe;
    private String trade_type;
    private String bank_type;
    private Integer total_fee;
    private Integer cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;

    public Boolean isSuccessReq() {
        return "SUCCESS".equals(return_code);
    }

    public Boolean isSuccessPay() {
        return "SUCCESS".equals(result_code);
    }

    public Boolean isValidParam(String appId, String mchId, String tradeType) {
        return appId.equals(appid) && mchId.equals(mch_id) && tradeType.equals(trade_type);
    }


}
