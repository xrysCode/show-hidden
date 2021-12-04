package com.datalevel.showhiddencontrol.auth.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限组与功能关系，多对多
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel()
public class AuthFunDto extends AuthFunEntity {

    @ApiModelProperty(value = "权限组名")
    private String authName;

    @ApiModelProperty(value = "功能名")
    private String funName;


}
