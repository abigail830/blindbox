package com.github.tuding.blindbox.api;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.tuding.blindbox.infrastructure.repository.AdminUserRepository;
import com.github.tuding.blindbox.infrastructure.security.DefaultEncryptor;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;

@Slf4j
@Controller
public class AdminLoginHandler {

    @Autowired
    Jwt jwt;

    @Autowired
    AdminUserRepository adminUserRepository;

    @GetMapping("/index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/admin-ui/main")
    public String main(Model model, HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("adminToken".equals(cookie.getName())) {
                String token = cookie.getValue();
                DecodedJWT verify = jwt.verifyWxToken(token);
                model.addAttribute("user", verify.getClaim("user").asString());
            }
        }
        return "main";
    }

    @PostMapping("/login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    public RedirectView login(
            @RequestParam("username") String username,
            @RequestParam("password") String pwd,
            HttpServletResponse response,
            DefaultEncryptor defaultEncryptor) throws ParseException {
        if (adminUserRepository.isValidAdmin(username, pwd, defaultEncryptor)) {
            String token = jwt.generateAdminToken(username, "", 10);
            response.addCookie(new Cookie("adminToken", token));
            log.info("login successfully for {}", username);
            return new RedirectView("/admin-ui/main");
        } else {
            log.info("login failed for {}", username);
            return new RedirectView("/index");
        }
    }


}
