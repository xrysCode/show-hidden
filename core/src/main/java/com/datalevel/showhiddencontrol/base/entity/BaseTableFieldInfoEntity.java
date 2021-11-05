package com.datalevel.showhiddencontrol.base.entity;

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
 * 服务的所有字段及表信息
 * </p>
 *
 * @author xry
 * @since 2021-11-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_table_field_info")
@ApiModel(value="BaseTableFieldInfoEntity对象", description="服务的所有字段及表信息")
public class BaseTableFieldInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "默认0表示是表，其他代表字段")
    private Long parentId;

    @ApiModelProperty(value = "可以通过向上查找得到应用名")
    private Long serviceId;

    @ApiModelProperty(value = "字段名")
    private String tableFieldName;

    @ApiModelProperty(value = "属性名（一般是驼峰）")
    private String propertyName;

    @ApiModelProperty(value = "简短描述")
    private String simpleDesc;

    @ApiModelProperty(value = "详细说明")
    private String comment;

    @ApiModelProperty(value = "是否已经弃用")
    private Boolean isDeprecated;

    private LocalDateTime createTime;

    private String createUser;


}
