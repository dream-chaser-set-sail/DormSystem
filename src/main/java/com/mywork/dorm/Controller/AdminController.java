package com.mywork.dorm.Controller;

import com.mywork.dorm.Bean.Admin;
import com.mywork.dorm.Bean.Login;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RestController
public class AdminController {
    
    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public Result list(Query query){
        return adminService.list(query);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        return adminService.deleteById(id);
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        Result result = null;
        for (Integer id : ids) {
            result = adminService.deleteById(id);
        }
        return result;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        return adminService.add(admin);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        return adminService.update(admin);
    }

    /*@GetMapping("/adminInfo")
    public Result adminInfo(@RequestHeader(name = "Authorization")String token) {
        return adminService.adminInfo(token);
    }*/

    @PutMapping("/updatePass/{oldPass}/{newPass}")
    public Result updatePass(@PathVariable String oldPass, @PathVariable String newPass) {
        return adminService.updatePass(oldPass,newPass);
    }
}
