package com.mywork.dorm.Service;

import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import com.mywork.dorm.Mapper.SupervisorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Iterator;

@Service
public class StudentSupervisorService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    public Result list(Query query) {
        int Page = (query.getPage() - 1) * query.getLimit();
        query.setPage(Page);
        Integer count = studentMapper.listCount(query);
        List<Student> users = studentMapper.list(query);
        List<College> colleges = studentMapper.selectByCollege();
        List<Major> majors = studentMapper.selectByMajor(null);
        List<Dorm> dorms = moveMapper.selectByDorm();

        Integer i = 0;
        while (i < users.size()) {
            Student user = users.get(i);

            // 查找并设置学院名称
            for (College college : colleges) {
                if (college.getId().equals(user.getCollege())) {
                    user.setCollegeName(college.getName());
                    break; // 找到后退出循环
                }
            }

            // 查找并设置专业名称
            for (Major major : majors) {
                if (major.getId().equals(user.getMajor())) {
                    user.setMajorName(major.getName());
                    break; // 找到后退出循环
                }
            }

            if (user.getDormId() != null){
                for (Dorm dorm : dorms) {
                    if (dorm.getId().equals(user.getDormId())) {
                        String name = dorm.getBuildingName() + "-" + dorm.getRoomNumber();
                        user.setDormName(name);
                        break; // 找到后退出循环
                    }
                }
            }
            i++; // 增加索引
        }

        // 获取宿管负责的楼号
        Supervisor supervisor = supervisorMapper.selectByAccount(query.getAccount());
        String buildingName = supervisor.getBuildingName();

        Iterator<Student> iterator = users.iterator();
        while (iterator.hasNext()) {
            Student user = iterator.next();
            if (user.getDormId() != null) {
                String building = user.getDormName().substring(0, 3);
                if (!building.equals(buildingName)) {
                    iterator.remove(); // 移除不符合条件的用户
                }
            } else {
                iterator.remove();
            }

        }

        return Result.Page(users, count);
    }
}
