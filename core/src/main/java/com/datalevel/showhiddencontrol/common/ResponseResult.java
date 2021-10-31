package com.datalevel.showhiddencontrol.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ResponseResult<T> {
    @ApiModelProperty(value = "响应代码")
    private Integer code;
    @ApiModelProperty(value = "提示信息")
    private String message;
    @ApiModelProperty(value = "响应数据")
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