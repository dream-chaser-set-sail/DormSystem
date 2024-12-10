package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/selectByStuNum")
    public Result selectByStuNum() {
        return indexService.selectByStuNum();
    }
}
