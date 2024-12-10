package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.MyDormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myDorm")
public class MyDormController {

    @Autowired
    private MyDormService myDormService;

    @GetMapping("/selectByDormMessage/{account}")
    public Result selectByDormMessage(@PathVariable String account) {
        return myDormService.selectByDormMessage(account);
    }
}
