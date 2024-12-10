package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.RepairMapper;
import com.mywork.dorm.Mapper.RepairMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private StudentMapper studentMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = repairMapper.listCount(query);
        List<Repair> repairs = repairMapper.list(query);
        List<Dorm> dorms = moveMapper.selectByDorm();

        Integer i = 0;
        while (i < repairs.size()) {
            Repair repair = repairs.get(i);
            if (repair.getDormId() != null){
                for (Dorm dorm : dorms) {
                    if (dorm.getId().equals(repair.getDormId())) {
                        String name = dorm.getBuildingName() + "-" + dorm.getRoomNumber();
                        repair.setDormName(name);
                        break; // 找到后退出循环
                    }
                }
            }
            i++; // 增加索引
        }
        
        return Result.Page(repairs, count);
    }

    public Result deleteById(Integer id) {
        Integer num = repairMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Repair repair) {
        Student student = studentMapper.selectByName(repair.getName());
        repair.setDormId(student.getDormId());
        Integer num = repairMapper.add(repair);
        if (num != 0){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    public Result selectById(Integer id) {
        Repair repair = repairMapper.selectById(id);
        return Result.clazz(repair);
    }

    public Result update(Repair repair) {
        Integer num = repairMapper.updateByStatus(repair.getId());
        if (num != 0){
            return Result.ok("已维修");
        }
        return Result.error("操作失败");
    }
}
