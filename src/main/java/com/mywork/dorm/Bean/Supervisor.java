package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supervisor {
    private Integer id;
    private String account;
    private String name;
    private String password;
    private String gender;
    private Integer age;
    private String image;
    private String buildingName;
    private Date createTime;
}
