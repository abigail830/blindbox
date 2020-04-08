package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.domain.user.User;
import com.github.tuding.blindbox.domain.user.UserService;
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
@Api("用户相关接口")
public class WxUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiImplicitParam(name = "X-WX-Code", value = "wechat code for get openId", required = true,
            paramType = "header", dataTypeClass = String.class)
    @ApiOperation("用户微信登陆(不需要带token)")
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
    @ApiOperation("根据Token获取用户信息(需要带token)")
    public User getUserByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return userService.getUserByToken(token);
    }


    @PostMapping("/auth")
    @NeedWxVerifyToken
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "encryptedData", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "iv", required = true, dataType = "String")})
    @ApiOperation("获取用户微信授权信息并更新后台数据(需要带token)")
    public void decrypt(HttpServletRequest request) {
        String encryptedData = request.getHeader("encryptedData");
        String iv = request.getHeader("iv");
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);

        userService.wxAuth(token, encryptedData, iv);
    }

    @PostMapping("/share-activity/{activityId}")
    @NeedWxVerifyToken
    @ApiOperation("用户分享了活动咨询(需要带token，每日首次积分+10)")
    public void shareActivity(HttpServletRequest request, @PathVariable String activityId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        userService.shareActivity(token, activityId);
    }

    @PostMapping("/share-collection/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation("用户分享了图鉴(需要带token，每日首次积分+10)")
    public void shareCollection(HttpServletRequest request, @PathVariable String seriesId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        userService.shareCollection(token, seriesId);
    }

}
