package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repair {
    private Integer id;
    private String name;
    private String DormName;
    private Integer DormId;
    private String repair;
    private String status;
    private Date createTime;
}
