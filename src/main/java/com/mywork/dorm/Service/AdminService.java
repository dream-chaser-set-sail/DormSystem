package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.Admin;
import com.mywork.dorm.Bean.Login;
import com.mywork.dorm.Bean.Query;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Mapper.AdminMapper;
import com.mywork.dorm.Utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = adminMapper.listCount(query);
        List<Admin> admins = adminMapper.list(query);
        return Result.Page(admins, count);
    }

    public Result deleteById(Integer id) {
        Integer num = adminMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Admin admin) {
        Admin adminInfo = null;
        if (admin.getImage() == null){
            adminInfo = SetDefaultProfileImage.setHeadImage(admin);
        }
        // 生成并添加管理员账号
        adminInfo.setAccount(AccountUtil.Account(AccountUtil.ADMIN));
        Integer num = 0;
        while (true) {
            try {
                num = adminMapper.add(adminInfo);
                break; // 插入成功，退出循环
            } catch (DuplicateKeyException e) {
                // 捕获唯一约束冲突异常
                adminInfo.setAccount(AccountUtil.Account(AccountUtil.ADMIN)); // 生成新的唯一 ID
            }
        }

        if (num != 0){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    public Result update(Admin admin) {
        Integer num = adminMapper.update(admin);
        if (num != 0){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    public Result updatePass(String oldPass, String newPass) {
//        使用线程传递token
        Map<String, Object> tokens = ThreadLocalUtil.get();

        String account = (String) tokens.get("id");
        String msg = "";
        Admin admin = adminMapper.selectByPass(account);

        if (admin == null || !admin.getPassword().equals(oldPass)){
            msg = "原密码错误";
            return Result.error(msg);
        }
        Integer num = adminMapper.updatePass(newPass, admin.getId());

        if (num != 0){
            msg = "修改完成";
        }
        return Result.ok(msg);
    }
}
