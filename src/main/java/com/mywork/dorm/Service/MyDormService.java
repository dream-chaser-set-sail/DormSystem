package com.mywork.dorm.Service;

import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.DormMapper;
import com.mywork.dorm.Mapper.MoveMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyDormService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private MoveMapper moveMapper;

    @Autowired
    private DormMapper dormMapper;

    public Result selectByDormMessage(String account) {
        // 根据账号完善用户信息
        Student studentOld = studentMapper.selectByAccount(account);
        Student student = completeInformationForStu(studentOld);

        // 根据用户信息查找舍友
        List<Student> students = studentMapper.selectByDormId(student.getDormId());
        List<Student> studentList = completeInformationForStu(students);

        // 根据用户信息查找宿舍信息
        Dorm dorm = dormMapper.selectById(student.getDormId());
        MyDorm myDorm = new MyDorm(student.getDormName(), dorm.getCapacity(), dorm.getPeople(), dorm.getStatus(), studentList);

        return Result.clazz(myDorm);
    }

    public List<Student> completeInformationForStu(List<Student> studentList) {
        List<College> colleges = studentMapper.selectByCollege();
        List<Major> majors = studentMapper.selectByMajor(null);
        List<Dorm> dorms = moveMapper.selectByDorm();

        if (studentList != null) {
            Integer i = 0;
            while (i < studentList.size()) {
                Student user = studentList.get(i);

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

                if (user.getDormId() != null) {
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
        } else {
            throw new RuntimeException("parameter error");
        }

        return studentList;
    }

    public Student completeInformationForStu(Student student) {
        List<College> colleges = studentMapper.selectByCollege();
        List<Major> majors = studentMapper.selectByMajor(null);
        List<Dorm> dorms = moveMapper.selectByDorm();

        // 查找并设置学院名称
        for (College college : colleges) {
            if (college.getId().equals(student.getCollege())) {
                student.setCollegeName(college.getName());
            }
        }
        // 查找并设置专业名称
        for (Major major : majors) {
            if (major.getId().equals(student.getMajor())) {
                student.setMajorName(major.getName());
            }
        }
        if (student.getDormId() != null){
            for (Dorm dorm : dorms) {
                if (dorm.getId().equals(student.getDormId())) {
                    String name = dorm.getBuildingName() + "-" + dorm.getRoomNumber();
                    student.setDormName(name);
                }
            }
        }
        return student;
    }
}
