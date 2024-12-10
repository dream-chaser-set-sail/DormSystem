package com.mywork.dorm.Service;

import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.RepairMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class IndexService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private RepairMapper repairMapper;

    public Result selectByStuNum() {
        // 创建Map集合存储相应数据
        Map<String, Integer> map = new HashMap<>();

        // 查找学生数量
        map.put("学生数量", studentMapper.listCount(null));

        // 查找迁宿数量
        map.put("迁宿条数", moveMapper.listCount(null));

        // 查找报修记录
        map.put("报修条数", repairMapper.listCount(null));

        return Result.clazz(map);
    }
}
