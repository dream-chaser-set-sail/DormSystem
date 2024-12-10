package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Dorm;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Bean.Student;
import com.mywork.dorm.Service.DormService;
import com.mywork.dorm.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dorm")
@RestController
public class DormController {

    @Autowired
    private DormService dormService;

    @GetMapping("/list")
    public Result list(Query query){
        return dormService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return dormService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = dormService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Dorm dorm) {
        return dormService.add(dorm);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return dormService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Dorm dorm) {
        return dormService.update(dorm);
    }

    @GetMapping("/selectByBuildingName")
    public Result selectByBuildingName() {
        return dormService.selectByBuildingName();
    }
}
