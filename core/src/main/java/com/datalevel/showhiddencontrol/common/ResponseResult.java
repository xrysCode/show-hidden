package com.datalevel.showhiddencontrol.common;

import lombok.Data;

@Data
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseResult() {
        this.code = 200;
        this.message = "ok";
    }

    public ResponseResult(T data) {
        this.code = 200;
        this.message = "ok";
        this.data = data;
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }



}