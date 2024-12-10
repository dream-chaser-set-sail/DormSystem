package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Bean.Supervisor;
import com.mywork.dorm.Service.SupervisorService;
import com.mywork.dorm.Service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/supervisor")
@RestController
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping("/list")
    public Result list(Query query){
        return supervisorService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return supervisorService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = supervisorService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Supervisor supervisor) {
        return supervisorService.add(supervisor);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return supervisorService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Supervisor supervisor) {
        return supervisorService.update(supervisor);
    }
}
