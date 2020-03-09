package com.github.tuding.blindbox.api;


import com.github.tuding.blindbox.infrastructure.security.Jwt;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@RestController
@RequestMapping("/login")
public class AdminLoginHandler {

    @Autowired
    Jwt jwt;

    @PostMapping
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    public RedirectView login(
            @RequestParam("username") String secret,
            @RequestParam("password") String pwd,
            HttpServletResponse response) throws ParseException {
        //TODO: check pwd
        String token = jwt.generateAdminToken(secret, "", 10);
        response.addCookie(new Cookie("adminToken", token));
        return new RedirectView("/main.html");
    }


}
