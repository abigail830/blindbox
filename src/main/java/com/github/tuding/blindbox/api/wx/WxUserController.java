package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序相关登录和解密接口
 */
@RestController
@RequestMapping("/wx/users")
@Slf4j
@Api(value = "用户相关接口", description = "用户相关接口")
public class WxUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiImplicitParam(name = "X-WX-Code", value = "wechat code for get openId", required = true,
            paramType = "header", dataTypeClass = String.class)
    @ApiOperation(value = "用户微信登陆(不需要带token)")
    public String login(HttpServletRequest request) {

        String code = request.getHeader("X-WX-Code");
        log.info("user trying to login wxchat with code: {}", code);
        final String token = userService.login(code);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return JsonUtil.toJson(result);
    }

    @GetMapping("/by-token")
    @NeedWxVerifyToken
    @ApiOperation(value = "根据Token获取用户信息(需要带token)")
    public User getUserByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return userService.getUserByToken(token);
    }


    @PostMapping("/auth")
    @NeedWxVerifyToken
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "encryptedData", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "iv", required = true, dataType = "String")})
    @ApiOperation(value = "获取用户微信授权信息并更新后台数据(需要带token)")
    public void decrypt(HttpServletRequest request) {
        String encryptedData = request.getHeader("encryptedData");
        String iv = request.getHeader("iv");
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);

        userService.wxAuth(token, encryptedData, iv);
    }

    @PutMapping("/activities/id/{activityId}/accept-notify")
    @NeedWxVerifyToken
    @ApiOperation(value = "接受活动开始通知(需要带token）- under development")
    public void registerForActivityNotify(HttpServletRequest request,
                                          @PathVariable String activityId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        userService.acceptActivityNotify(token, activityId);

    }

}
