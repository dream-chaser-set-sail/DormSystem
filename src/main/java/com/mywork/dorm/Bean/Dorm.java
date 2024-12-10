package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dorm {
    private Integer id;
    private String buildingName;
    private String roomNumber;
    private Integer capacity;
    private Integer people;
    private String status;
}
