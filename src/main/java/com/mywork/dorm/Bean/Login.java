package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String account;
    private String name;
    private String password;
    private String code;
}
