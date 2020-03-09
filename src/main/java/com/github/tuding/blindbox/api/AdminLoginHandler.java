package com.github.tuding.blindbox.api;


import com.github.tuding.blindbox.filter.IgnoreWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.repository.AdminUserRepository;
import com.github.tuding.blindbox.infrastructure.security.DefaultEncryptor;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/login")
public class AdminLoginHandler {

    @Autowired
    Jwt jwt;

    @Autowired
    AdminUserRepository adminUserRepository;


    @PostMapping
    @IgnoreWxVerifyToken
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
            return new RedirectView("/main.html");
        } else {
            log.info("login failed for {}", username);
            return new RedirectView("/index.html");
        }
    }


}
