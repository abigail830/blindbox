package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserService;
import com.github.tuding.blindbox.filter.IgnoreWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序相关登录和解密接口
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @IgnoreWxVerifyToken
    @GetMapping("/login-wx")
    @ApiImplicitParam(name = "X-WX-Code", value = "wechat code for get openId", required = true,
            paramType = "header", dataTypeClass = String.class)
    public String login(HttpServletRequest request) {

        String code = request.getHeader("X-WX-Code");
        log.info("user trying to login wxchat with code: {}", code);
        final String token = userService.login(code);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return JsonUtil.toJson(result);
    }

    @GetMapping("/token")
    public User getUserByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return userService.getUserByToken(token);
    }


    @GetMapping("/decrypt")
    public String decrypt(HttpServletRequest request) {
//        String skey = request.getHeader("skey");
//        String encryptedData = request.getHeader("encryptedData");
//        String iv = request.getHeader("iv");
//        WXBizDataCrypt biz = new WXBizDataCrypt(appId, skey);
//
//        String resultDate = biz.decryptData(encryptedData, iv);
//        WxDecryptResponse wxDecryptResponse = JsonUtil.toObject(resultDate, WxDecryptResponse.class);
//        log.info("wxDecryptResponse: {}", wxDecryptResponse);
//        if (wxDecryptResponse.getErrorCode() == null) {
//            userService.updateUser(wxDecryptResponse.getUserInfo());
//            log.info("Updated user info for user {}", wxDecryptResponse.getUserInfo().getOpenId());
//        } else {
//            log.error("Error occur during decrypt wechat message with error code: {}", wxDecryptResponse.getErrorCode());
//        }
//
//        return resultDate;
        return "Hello";
    }
}
