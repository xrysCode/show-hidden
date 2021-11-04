package com.datalevel.showhiddencontrol.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessException extends RuntimeException{
    private int code=1000;
    public BusinessException(String message) {
        super(message);
        this.code=1000;
    }
    public BusinessException(int code, String message) {
        super(message);
        if(code<1000){
            log.error("业务状态码必须大于等于1000");
            throw new RuntimeException("业务状态码必须大于等于1000");
        }
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
