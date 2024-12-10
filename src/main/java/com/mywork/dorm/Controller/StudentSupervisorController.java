package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Bean.Student;
import com.mywork.dorm.Service.StudentService;
import com.mywork.dorm.Service.StudentSupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/studentSupervisor")
@RestController
public class StudentSupervisorController {

    @Autowired
    private StudentSupervisorService studentSupervisorService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    public Result list(Query query){
        return studentSupervisorService.list(query);
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
