package com.mywork.dorm.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code = null;
    private String msg = null;
    private List<T> data = null;
    private Object dataClass = null;
    private Integer count = null;

    public static final Integer ERROR = 1;
    public static final Integer INFO = 0;

    public static <T> Result<T> Page(List<T> data, Integer count){
        return new Result(INFO, null, data, null, count);
    }

    public static Result ok(String msg) {
        return new Result(INFO, msg, null, null, null);
    }

    public static Result error(String msg) {
        return new Result(ERROR, msg, null, null, null);
    }

    public static Result clazz(Object data) {
        return new Result(INFO, null, null, data, null);
    }

    public static<T> Result<T> selList(List<T> data) {
        return new Result(INFO, null, data, null, null);
    }

    public static Result loginMsg(Integer code, String msg, String token) {
        return new Result(code, msg, null, token, null);
    }

}
