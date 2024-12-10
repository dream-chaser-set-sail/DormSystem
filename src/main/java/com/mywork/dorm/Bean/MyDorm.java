package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyDorm {
    private String dormName;
    private Integer capacityNum;
    private Integer peopleNum;
    private String status;
    private List<Student> students;
}
