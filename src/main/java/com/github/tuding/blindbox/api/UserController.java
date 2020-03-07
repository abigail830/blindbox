package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.domain.UserService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 小程序相关登录和解密接口
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/login-wx")
    @ApiImplicitParam(name = "X-WX-Code", value = "wechat code for get openId", required = true,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String login(HttpServletRequest request) {
        String code = request.getHeader("X-WX-Code");
        log.info("user trying to login wxchat with code: {}", code);

        return userService.login(code).getJwtToken();
    }

//    @GetMapping("/decrypt")
//    public String decrypt(HttpServletRequest request) {
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
//    }
}
