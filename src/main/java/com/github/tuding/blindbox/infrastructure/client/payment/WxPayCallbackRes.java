package com.github.tuding.blindbox.infrastructure.client.payment;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WxPayCallbackRes {

    String return_code;
    String return_msg;

    public WxPayCallbackRes(String return_code) {
        this.return_code = return_code;
    }

    public static WxPayCallbackRes buildSuccess() {
        return new WxPayCallbackRes("SUCCESS");
    }

    public static WxPayCallbackRes buildFail(String msg) {
        return new WxPayCallbackRes("FAIL", msg);
    }
}
