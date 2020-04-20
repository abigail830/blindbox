package com.github.tuding.blindbox.infrastructure.client.payment;

import lombok.*;

import java.util.Map;

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

    public WxPayCallbackReq(Map<String, String> map) {
        this.return_code = map.containsKey("return_code") ? return_code : null;
        this.return_msg = map.containsKey("return_msg") ? return_msg : null;
        this.result_code = map.containsKey("result_code") ? result_code : null;
        this.appid = map.containsKey("appid") ? appid : null;
        this.mch_id = map.containsKey("mch_id") ? mch_id : null;
        this.sign = map.containsKey("sign") ? sign : null;
        this.nonce_str = map.containsKey("nonce_str") ? nonce_str : null;
        this.openid = map.containsKey("openid") ? openid : null;
        this.is_subscribe = map.containsKey("is_subscribe") ? is_subscribe : null;
        this.trade_type = map.containsKey("trade_type") ? trade_type : null;
        this.bank_type = map.containsKey("bank_type") ? bank_type : null;
        this.total_fee = map.containsKey("total_fee") ? total_fee : null;
        this.cash_fee = map.containsKey("cash_fee") ? cash_fee : null;
        this.transaction_id = map.containsKey("transaction_id") ? transaction_id : null;
        this.out_trade_no = map.containsKey("out_trade_no") ? out_trade_no : null;
        this.time_end = map.containsKey("time_end") ? time_end : null;
    }

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
