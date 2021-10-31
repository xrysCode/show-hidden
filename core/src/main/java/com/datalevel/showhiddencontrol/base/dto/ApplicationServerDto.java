package com.datalevel.showhiddencontrol.base.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApplicationServerDto extends BaseApplicationEntity {
    @ApiModelProperty(value = "服务名")
    private String serviceName;
    @ApiModelProperty(value = "服务简述")
    private String serviceDesc;
}
