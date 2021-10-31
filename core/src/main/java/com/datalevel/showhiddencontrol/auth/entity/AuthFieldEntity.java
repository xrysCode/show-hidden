package com.datalevel.showhiddencontrol.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字段控制，不同的权限组需要不同的字段
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("auth_field")
@ApiModel(value="AuthFieldEntity对象", description="字段控制，不同的权限组需要不同的字段")
public class AuthFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long authId;

    private Long fieldId;

    private Long funId;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean isHide;

    @ApiModelProperty(value = "是否允许修改")
    private Boolean isModify;

    @ApiModelProperty(value = "逻辑（>,>=,=,<=,<,其值为null时表示不需要）")
    private String logic;

    @ApiModelProperty(value = "权限值，（预制关键字SELF/CURRENT_TIMESTAMP）当满足这个值时可以查询")
    private String authValue;

    private LocalDateTime createTime;

    private String createUser;


}
