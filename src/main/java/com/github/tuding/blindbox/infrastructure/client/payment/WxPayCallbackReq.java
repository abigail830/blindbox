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
    private String is_subscribe;
    private String trade_type;
    private String bank_type;
    private Integer total_fee;
    private Integer cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;
    private String fee_type;


    public WxPayCallbackReq(Map<String, String> map) {
        this.return_code = map.getOrDefault("return_code", null);
        this.return_msg = map.getOrDefault("return_msg", null);
        this.result_code = map.getOrDefault("result_code", null);
        this.appid = map.getOrDefault("appid", null);
        this.mch_id = map.getOrDefault("mch_id", null);
        this.sign = map.getOrDefault("sign", null);
        this.nonce_str = map.getOrDefault("nonce_str", null);
        this.openid = map.getOrDefault("openid", null);
        this.is_subscribe = map.getOrDefault("is_subscribe", null);
        this.trade_type = map.getOrDefault("trade_type", null);
        this.bank_type = map.getOrDefault("bank_type", null);
        this.total_fee = Integer.valueOf(map.getOrDefault("total_fee", "0"));
        this.cash_fee = Integer.valueOf(map.getOrDefault("cash_fee", "0"));
        this.transaction_id = map.getOrDefault("transaction_id", null);
        this.out_trade_no = map.getOrDefault("out_trade_no", null);
        this.time_end = map.getOrDefault("time_end", null);
        this.fee_type = map.getOrDefault("fee_type", null);
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
