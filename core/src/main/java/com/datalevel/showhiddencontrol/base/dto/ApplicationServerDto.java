package com.datalevel.showhiddencontrol.base.dto;

import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ApplicationServerDto", description="")
public class ApplicationServerDto extends BaseApplicationEntity {
    @ApiModelProperty(value = "服务名")
    private String serviceName;
    @ApiModelProperty(value = "服务简述")
    private String serviceDesc;
}
