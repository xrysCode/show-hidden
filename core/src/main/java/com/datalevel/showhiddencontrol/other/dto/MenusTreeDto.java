package com.datalevel.showhiddencontrol.other.dto;

import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MenusTreeDto对象", description="")
public class MenusTreeDto extends OtherMenusEntity {
    @ApiModelProperty(value = "功能模块子节点")
    private List<MenusTreeDto> children;
}
