package com.datalevel.showhiddencontrol.base.dto;

import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FunctionModuleSaveDto", description="")
public class FunctionModuleSaveDto extends BaseFunctionModuleEntity {
    @ApiModelProperty(value = "表或字段id",notes = "api接口时必须是表，否则为组件字段")
    private List<Long> fieldIds;
//    @ApiModelProperty(value = "功能模块id")
//    private Long funId;

}
