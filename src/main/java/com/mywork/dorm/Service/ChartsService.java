package com.mywork.dorm.Service;

import com.mywork.dorm.Bean.Dorm;
import com.mywork.dorm.Bean.Result;
import com.mywork.dorm.Mapper.DormMapper;
import com.mywork.dorm.Mapper.MoveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartsService {

    @Autowired
    private DormMapper dormMapper;

    public Result selectByData() {
        // 创建Map集合存储图表的X轴Y轴数据
        Map<String, Integer> map = new HashMap<>();
        // 查询所有宿舍信息
        List<Dorm> dorms = dormMapper.selectByCharts();

        for (Dorm dorm : dorms) {
            map.put(dorm.getBuildingName(), dorm.getPeople());
        }

        return Result.clazz(map);
    }
}
