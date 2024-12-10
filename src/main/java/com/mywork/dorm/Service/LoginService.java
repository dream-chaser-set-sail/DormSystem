package com.mywork.dorm.Service;

import com.mywork.dorm.Bean.*;
import com.mywork.dorm.Mapper.AdminMapper;
import com.mywork.dorm.Mapper.StudentMapper;
import com.mywork.dorm.Mapper.SupervisorMapper;
import com.mywork.dorm.Utils.AccountUtil;
import com.mywork.dorm.Utils.CommonUtil;
import com.mywork.dorm.Utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SupervisorMapper supervisorMapper;

    @Autowired
    private AdminMapper adminMapper;

    public Result login(Login login, HttpServletRequest request) {
        // 起始配置
        Map<String, Object> map = new HashMap(); // token存储的数据
        int code = 1;
        String token = "";
        String msg = "账号有误，请重试";
        // 获取captcha
        String captcha = (String) redisTemplate.opsForValue().get(CommonUtil.getCaptchaKey(request));
        Student student = null;
        Supervisor supervisor = null;
        Admin admin = null;

        // 确认登陆者身份
        Integer role = AccountUtil.selectByRole(login.getAccount());
            // 根据身份查询相应数据库
            switch (role) {
                case 1:
                    student = studentMapper.selectByLogin(login);
                    msg = "未识别到用户，请检查账号或姓名是否正确";
                    if (student != null) {
                        if (!student.getPassword().equals(login.getPassword())) {
                            msg = "密码错误请重试";
                        }else if (!captcha.equalsIgnoreCase(login.getCode())){
                            msg = "验证码错误";
                        }else if (StringUtils.isEmpty(captcha)){
                            msg = "验证码已经失效";
                        }else {
                            msg = "登录成功";
                            code = 0;
                            map.put("id", student.getAccount());
                            map.put("name", student.getName());
                            token = JwtUtil.createToken(map);
                        }
                    }
                    break;
                case 2:
                    supervisor = supervisorMapper.selectByLogin(login);
                    msg = "未识别到用户，请检查账号或姓名是否正确";
                    if (supervisor != null) {
                        if (!supervisor.getPassword().equals(login.getPassword())) {
                            msg = "密码错误请重试";
                        }else if (!captcha.equalsIgnoreCase(login.getCode())){
                            msg = "验证码错误";
                        }else if (StringUtils.isEmpty(captcha)){
                            msg = "验证码已经失效";
                        }else {
                            msg = "登录成功";
                            code = 0;
                            map.put("id", supervisor.getAccount());
                            map.put("name", supervisor.getName());
                            token = JwtUtil.createToken(map);
                        }
                    }
                    break;
                case 3:
                    admin = adminMapper.selectByLogin(login);
                    msg = "未识别到用户，请检查账号或姓名是否正确";
                    if (admin != null) {
                        if (!admin.getPassword().equals(login.getPassword())) {
                            msg = "密码错误请重试";
                        }else if (!captcha.equalsIgnoreCase(login.getCode())){
                            msg = "验证码错误";
                        }else if (StringUtils.isEmpty(captcha)){
                            msg = "验证码已经失效";
                        }else {
                            msg = "登录成功";
                            code = 0;
                            map.put("id", admin.getAccount());
                            map.put("name", admin.getName());
                            token = JwtUtil.createToken(map);
                        }
                    }
                    break;
                default:
                    msg = "未识别到用户，请检查账号";
            }
        return Result.loginMsg(code, msg, token);
    }

    public Result loginInfo(String token) {
        // 获取token存储的账号
        String account = (String) JwtUtil.parseToken(token).get("id");

        // 根据获取的account判断身份
        Integer roleNum = AccountUtil.selectByRole(account);
        switch (roleNum) {
            case 1:
                return getStudentInfo(account);
            case 2:
                return getSupervisorInfo(account);
            case 3:
                return getAdminInfo(account);
            default:
                throw new IllegalArgumentException("Invalid role parameter: " + roleNum);
        }
    }

    private Result getStudentInfo(String account) {
        Student student = studentMapper.selectByAccount(account);
        if (student != null) {
            student.setPassword("***");
            return Result.clazz(student);
        }
        return Result.error("没有找到学生");
    }

    private Result getSupervisorInfo(String account) {
        Supervisor supervisor = supervisorMapper.selectByAccount(account);
        if (supervisor != null) {
            supervisor.setPassword("***");
            return Result.clazz(supervisor);
        }
        return Result.error("没有找到宿管");
    }

    private Result getAdminInfo(String account) {
        Admin admin = adminMapper.selectById(account);
        if (admin != null) {
            admin.setPassword("***");
            return Result.clazz(admin);
        }
        return Result.error("没有找到管理员");
    }

}
