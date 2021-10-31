package com.datalevel.showhiddencontrol.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
public class ResponsePage<T> extends RequestPage<T>{

    @ApiModelProperty(value = "总数")
    protected long total = 0;

    @ApiModelProperty(value = "查询数据列表")
    protected List<T> records = Collections.emptyList();

}