package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Login;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody Login login, HttpServletRequest request) {
        return loginService.login(login, request);
    }

    @GetMapping("/loginInfo")
    public Result adminInfo(@RequestHeader(name = "Authorization")String token) {
        return loginService.loginInfo(token);
    }
}
