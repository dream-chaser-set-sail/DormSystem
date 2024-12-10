package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Bean.Student;
import com.mywork.dorm.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/student")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public Result list(Query query){
        return studentService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return studentService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = studentService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        return studentService.add(student);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return studentService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @GetMapping("/selectByCollege")
    public Result selectByCollege() {
        return studentService.selectByCollege();
    }

    @GetMapping("/selectByMajor/{id}")
    public Result selectByMajor(@PathVariable Integer id) {
        return studentService.selectByMajor(id);
    }
}
