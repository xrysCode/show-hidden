package com.datalevel.showhiddencontrol.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务表
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_application")
@ApiModel(value="BaseApplicationEntity对象", description="应用表")
public class BaseApplicationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;

    @NotNull(groups = {Update.class, Insert.class})
    @ApiModelProperty(value = "应用名")
    private String appName;

    @ApiModelProperty(value = "服务简述")
    private String appDesc;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private String createUser;


}
