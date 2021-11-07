package com.datalevel.showhiddencontrol.base.dto;

import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TableFieldInfoDto", description="")
public class TableFieldInfoDto extends BaseTableFieldInfoEntity {
    @ApiModelProperty(value = "是否选中")
    private Boolean isSelected;
}
