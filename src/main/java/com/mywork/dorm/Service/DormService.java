package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.DormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DormService {

    @Autowired
    private DormMapper dormMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = dormMapper.listCount(query);
        List<Dorm> dorms = dormMapper.list(query);
        return Result.Page(dorms, count);
    }

    public Result deleteById(Integer id) {
        Integer num = dormMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Dorm dorm) {
        Integer num = dormMapper.add(dorm);
        if (num != 0){
            return Result.ok("补录成功");
        }
        return Result.error("补录失败");
    }

    public Result selectById(Integer id) {
        Dorm dorm = dormMapper.selectById(id);
        return Result.clazz(dorm);
    }

    public Result update(Dorm dorm) {
        Integer num = dormMapper.update(dorm);
        if (num != 0){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    public Result selectByBuildingName() {
        List<BuildingName> buildingNames = dormMapper.selectByBuildingName();
        for (int i = 0; i < buildingNames.size(); i++) {
            buildingNames.get(i).setId(i+1);
        }
        return Result.selList(buildingNames);
    }
}
