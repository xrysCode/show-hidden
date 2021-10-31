package com.datalevel.showhiddencontrol.base.dto;

import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FunctionModuleTreeDto extends BaseFunctionModuleEntity {
    @ApiModelProperty(value = "功能模块子节点")
    private List<FunctionModuleTreeDto> children;



}
