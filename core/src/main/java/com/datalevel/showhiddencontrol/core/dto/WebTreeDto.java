package com.datalevel.showhiddencontrol.core.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.datalevel.showhiddencontrol.core.entity.WebComponentTreeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class WebTreeDto extends WebComponentTreeEntity {
    @ApiModelProperty(value = "子级")

    private List<WebTreeDto> childNodes;
}
