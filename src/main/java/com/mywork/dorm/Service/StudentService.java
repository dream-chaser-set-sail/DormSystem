package com.mywork.dorm.Service;


import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import com.mywork.dorm.Mapper.SupervisorMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.JwtUtil;
import com.mywork.dorm.Utils.SetDefaultProfileImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StudentService {

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
        List<College> colleges = studentMapper.selectByCollege();
        List<Major> majors = studentMapper.selectByMajor(null);
        List<Student> users = studentMapper.list(query);
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

        return Result.Page(users, count);
    }

    public Result deleteById(Integer id) {
        Integer num = studentMapper.deleteById(id);
        if (num != 0){
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    public Result add(Student student) {
        Student studentInfo = null;
        if (student.getImage() == null){
            studentInfo = SetDefaultProfileImage.setHeadImage(student);
        }
        // 生成并添加管理员账号
        studentInfo.setAccount(AccountUtil.Account(AccountUtil.STUDENT));
        Integer num = 0;
        while (true) {
            try {
                num = studentMapper.add(studentInfo);
                break; // 插入成功，退出循环
            } catch (DuplicateKeyException e) {
                // 捕获唯一约束冲突异常
                studentInfo.setAccount(AccountUtil.Account(AccountUtil.STUDENT)); // 生成新的唯一 ID
            }
        }

        if (num != 0){
            return Result.ok("添加成功");
        }
        return Result.error("添加失败");
    }

    public Result selectById(Integer id) {
        Student student = studentMapper.selectById(id);
        return Result.clazz(student);
    }

    public Result update(Student student) {
        Integer num = studentMapper.update(student);
        if (num != 0){
            return Result.ok("修改成功");
        }
        return Result.error("修改失败");
    }

    public Result selectByCollege() {
        List<College> colleges = studentMapper.selectByCollege();
        return Result.selList(colleges);
    }

    public Result selectByMajor(Integer id) {
        List<Major> majors = studentMapper.selectByMajor(id);
        return Result.selList(majors);
    }

    public Result selectByName(String token) {
        List<DormName> dormNames = studentMapper.selectToName();
        for (int i = 0; i < dormNames.size(); i++) {
            dormNames.get(i).setId(i+1);
        }

        // 宿管所负责的楼层
        String buildingForSupervisor = "";

        // 根据token获取token内存储的账号
        String account = (String) JwtUtil.parseToken(token).get("id");
        Integer roleNum = AccountUtil.selectByRole(account);
        if (roleNum == 2) { // 如果是宿管操作
            Supervisor supervisor = supervisorMapper.selectByAccount(account);
            buildingForSupervisor = supervisor.getBuildingName();

            // 查询并删选是否有宿管管理楼号下的学生
            List<DormName> dormNamesResult = selectByBuilding(dormNames, buildingForSupervisor);
            return Result.selList(dormNamesResult);
        }

        return Result.selList(dormNames);
    }

    public List<DormName> selectByBuilding(List<DormName> dormNameList, String buildingForSupervisor) {
        // 查询宿舍表信息
        List<Dorm> dorms = moveMapper.selectByDorm();
        // 存储学生信息
        List<Student> studentList = new ArrayList<>();

        for (DormName dormName : dormNameList) {
            // 通过dormList内存储的学生姓名查询学生信息并存入集合
            Student student = studentMapper.selectByNameForBuilding(dormName.getName());
            for (Dorm dorm : dorms) {
                if (dorm.getId().equals(student.getDormId())) { // 通过学生宿舍id关联出学生所在宿舍
                    String name = dorm.getBuildingName() + "-" + dorm.getRoomNumber();
                    student.setDormName(name);
                    break;
                }
            }
            studentList.add(student);
        }

        // 使用 Iterator 来移除不符合条件的用户
        Iterator<DormName> iterator = dormNameList.iterator();
        while (iterator.hasNext()) {
            DormName user = iterator.next();
            boolean found = false; // 标志是否找到匹配的学生

            // 遍历学生列表查找匹配的学生
            for (Student student : studentList) {
                if (student.getDormName() != null) {
                    // 获取学生宿舍的楼号
                    String stuBuilding = student.getDormName().substring(0, 3);
                    // 如果学生宿舍与管理员所管理的宿舍楼号一致，并且姓名匹配
                    if (stuBuilding.equals(buildingForSupervisor) && user.getName().equals(student.getName())) {
                        found = true; // 找到匹配的学生
                        break; // 跳出内层循环
                    }
                } else {
                    found = false;
                }
            }

            // 如果没有找到匹配的学生，则移除该用户
            if (!found) {
                iterator.remove(); // 移除不符合条件的用户
            }
        }
        return dormNameList;
    }
}
