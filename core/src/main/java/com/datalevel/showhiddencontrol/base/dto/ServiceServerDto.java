package com.datalevel.showhiddencontrol.base.dto;

import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ServiceServerDto", description="")
public class ServiceServerDto extends BaseServiceEntity {
    @ApiModelProperty(value = "应用")
    private String appName;
    @ApiModelProperty(value = "应用简述")
    private String appDesc;
}
