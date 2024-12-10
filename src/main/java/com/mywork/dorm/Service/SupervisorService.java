package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.SupervisorMapper;
import com.mywork.dorm.Mapper.SupervisorMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.SetDefaultProfileImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupervisorService {

    @Autowired
    private SupervisorMapper supervisorMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = supervisorMapper.listCount(query);
        List<Supervisor> supervisors = supervisorMapper.list(query);
        return Result.Page(supervisors, count);
    }

    public Result deleteById(Integer id) {
        Integer num = supervisorMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Supervisor supervisor) {
        Supervisor supervisorInfo = null;
        if (supervisor.getImage() == null){
            supervisorInfo = SetDefaultProfileImage.setHeadImage(supervisor);
        }

        supervisorInfo.setAccount(AccountUtil.Account(AccountUtil.SUPERVISOR));
        Integer num = 0;
        while (true) {
            try {
                num = supervisorMapper.add(supervisorInfo);
                break; // 插入成功，退出循环
            } catch (DuplicateKeyException e) {
                // 捕获唯一约束冲突异常
                supervisorInfo.setAccount(AccountUtil.Account(AccountUtil.SUPERVISOR)); // 生成新的唯一 ID
            }
        }
        if (num != 0){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    public Result selectById(Integer id) {
        Supervisor supervisor = supervisorMapper.selectById(id);
        return Result.clazz(supervisor);
    }

    public Result update(Supervisor supervisor) {
        Integer num = supervisorMapper.update(supervisor);
        if (num != 0){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }
}
