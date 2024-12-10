package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String account;
    private String password;
    private String image;
    private String email;
    private Integer college;
    private Integer major;
    private String collegeName;
    private String majorName;
    private Integer dormId;
    private String dormName;
    private Date createTime;
}
