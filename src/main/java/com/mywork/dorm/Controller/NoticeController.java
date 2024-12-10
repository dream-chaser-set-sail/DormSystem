package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Notice;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Bean.Student;
import com.mywork.dorm.Service.NoticeService;
import com.mywork.dorm.Service.StudentService;
import org.apache.ibatis.annotations.Delete;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notice")
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public Result list(Query query){
        return noticeService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return noticeService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = noticeService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        return noticeService.add(notice);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return noticeService.selectById(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
        return noticeService.update(notice);
    }

    @DeleteMapping("/deleted/{id}/{deleted}")
    public Result deleted(@PathVariable Integer id, @PathVariable Integer deleted) {
        return noticeService.deleted(id, deleted);
    }

    @GetMapping("/selectByAllData")
    public Result selectByAllData() {
        return noticeService.selectByAllData();
    }
}
