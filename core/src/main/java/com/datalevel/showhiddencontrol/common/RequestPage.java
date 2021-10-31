package com.datalevel.showhiddencontrol.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class RequestPage<T> {
    @Min(1)
    @ApiModelProperty(value = "当前页",example="1")
    protected long current = 1;

    @Min(1)
    @ApiModelProperty(value = "每页显示条数，默认 10",example="10")
    protected long size = 10;

}