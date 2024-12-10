package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Move;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Repair;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.MoveService;
import com.mywork.dorm.Service.RepairService;
import com.mywork.dorm.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/repair")
@RestController
public class RepairController {

    @Autowired
    private RepairService repairService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public Result list(Query query){
        return repairService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return repairService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = repairService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Repair repair) {
        return repairService.add(repair);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return repairService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Repair repair) {
        return repairService.update(repair);
    }

    @GetMapping("/selectByName")
    public Result selectByName(@RequestHeader(name = "Authorization")String token) {
        return studentService.selectByName(token);
    }
}
