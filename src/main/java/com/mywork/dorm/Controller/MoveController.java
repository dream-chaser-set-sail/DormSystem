package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Move;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.MoveService;
import com.mywork.dorm.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/move")
@RestController
public class MoveController {

    @Autowired
    private MoveService moveService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public Result list(Query query){
        return moveService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return moveService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = moveService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Move move) {
        return moveService.add(move);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return moveService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Move move) {
        return moveService.update(move);
    }

    @GetMapping("/selectByDorm")
    public Result selectByDorm() {
        return moveService.selectByDorm();
    }

    @GetMapping("/selectByName")
    public Result selectByName() {
        return moveService.selectByName();
    }

    @PutMapping("/updateByNo")
    public Result updateByNo(@RequestBody Move move) {
        return moveService.updateByNo(move);
    }

}
