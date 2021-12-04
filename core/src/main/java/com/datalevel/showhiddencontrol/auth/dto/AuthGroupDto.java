package com.datalevel.showhiddencontrol.auth.dto;

import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel
@Data
@Accessors(chain = true)
public class AuthGroupDto extends AuthGroupEntity {
    @ApiModelProperty(value = "app service 名称")
    private String appServiceName;

}
