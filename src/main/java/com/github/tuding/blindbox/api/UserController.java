package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserService;
import com.github.tuding.blindbox.filter.IgnoreWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.util.JsonUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @PostMapping("/login/wx")
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
    public ResponseEntity<User> getUserByToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final Optional<User> userByToken = userService.getUserByToken(token);
        if (userByToken.isPresent()) {
            return new ResponseEntity<>(userByToken.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @PostMapping("/auth/wx")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "encryptedData", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "header", name = "iv", required = true, dataType = "String")})
    public void decrypt(HttpServletRequest request) {
        String encryptedData = request.getHeader("encryptedData");
        String iv = request.getHeader("iv");
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);

        userService.wxAuth(token, encryptedData, iv);
    }
}
