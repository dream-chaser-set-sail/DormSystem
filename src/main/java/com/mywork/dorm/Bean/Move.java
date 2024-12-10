package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {
    private Integer id;
    private Integer sid;
    private String name;
    private Integer beforeDormId;
    private String beforeDormName;
    private Integer moveDormId;
    private String moveDormName;
    private String status;
    private Date createTime;
    // Query
    private String account;
}
