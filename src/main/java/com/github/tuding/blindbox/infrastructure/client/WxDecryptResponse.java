package com.github.tuding.blindbox.infrastructure.client;

import com.github.tuding.blindbox.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@ApiModel("微信信息解密返回结果")
public class WxDecryptResponse {

    private String openId;
    private String gender;
    private String nickName;
    private String city;
    private String country;
    private String province;
    private String avatarUrl;
    private String unionId;

    public User toUser() {
        return new User(this.openId, this.gender, this.nickName,
                this.city, this.country, this.province, this.avatarUrl, this.unionId);
    }
}
