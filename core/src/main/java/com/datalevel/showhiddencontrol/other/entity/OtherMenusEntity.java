package com.datalevel.showhiddencontrol.other.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author xry
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "other_menus",autoResultMap=true)
@ApiModel(value="OtherMenusEntity对象", description="")
public class OtherMenusEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;

    @ApiModelProperty(value = "父级菜单id")
    @NotNull(groups = {Insert.class, Update.class})
    private Long parentId;

    @ApiModelProperty(value = "菜单名")
    @NotNull(groups = {Insert.class, Update.class})
    private String menuName;

    @ApiModelProperty(value = "菜单描述")
    private String menuDesc;

    @ApiModelProperty(value = "路由路径")
    private String routerPath;

    @ApiModelProperty(value = "菜单排序")
    @NotNull(groups = {Insert.class})
    private Integer sort;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty(value = "权限appKeys")
    private List<String> authAppKeys;

    @ApiModelProperty(value = "权限serviceKeys")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authServiceKeys;

    private LocalDateTime createTime;

    private String createUser;


}
