package com.datalevel.showhiddencontrol.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("auth_group")
@ApiModel(value="AuthGroupEntity对象", description="权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。")
public class AuthGroupEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限组名")
    @NotBlank(groups = {Update.class, Insert.class})
    private String authName;

    @ApiModelProperty(value = "code编码，数据级使用，目的降低字符长度")
    private Integer authCode;

    @ApiModelProperty(value = "组描述")
    private String authDesc;

    @ApiModelProperty(value = "app service 类型，枚举(app，service)")
    @TableField(typeHandler = MybatisEnumTypeHandler.class)
    @NotNull(groups = {Update.class, Insert.class})
    private AppServiceEnum appServiceType;

    @ApiModelProperty(value = "app service id 不同维度条件不同")
    @NotNull(groups = {Update.class, Insert.class})
    private Long appServiceId;

    private LocalDateTime createTime;

    private String createUser;


}
