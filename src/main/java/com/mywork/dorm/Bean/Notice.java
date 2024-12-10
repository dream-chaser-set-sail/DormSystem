package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    private Integer id;
    private String title;
    private String content;
    private String accountNum;
    private String persistentTime;
    private Integer deleted;
    private String status;
    private Date createTime;
    private Date updateTime;
    // Query
    private String[] persistentTimeArray;
    private String name;
}
