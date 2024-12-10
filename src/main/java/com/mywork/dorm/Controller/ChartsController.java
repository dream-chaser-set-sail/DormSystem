package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/charts")
public class ChartsController {

    @Autowired
    private ChartsService chartsService;

    @GetMapping("/selectByData")
    public Result selectByData() {
        return chartsService.selectByData();
    }
}
