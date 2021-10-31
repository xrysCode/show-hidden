package com.datalevel.showhiddencontrol.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TreeShiftDto {
    @ApiModelProperty(value = "拖拽的节点")
    @NotNull
    private Long draggingId;
    @ApiModelProperty(value = "替换的节点,将下移")
    @NotNull
    private Long replaceId;

}
