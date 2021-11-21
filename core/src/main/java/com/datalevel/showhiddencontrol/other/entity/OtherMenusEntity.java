package com.datalevel.showhiddencontrol.other.entity;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ApiModelProperty(value = "组件props 属性json")
    @JsonIgnore
    private String comProps;

    @ApiModelProperty(value = "组件位置路径")
    private String comImport;

    @ApiModelProperty(value = "菜单排序")
    @NotNull(groups = {Insert.class})
    private Integer sort;

    @ApiModelProperty(value = "权限keys")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authKeys;

    private LocalDateTime createTime;

    private String createUser;


    @ApiModelProperty(value = "组件props 属性json")
    public Map<String,Object> getComponentProps() {
        return JSONUtil.toBean(comProps, HashMap.class);
    }
    @ApiModelProperty(value = "组件props 属性json")
    public OtherMenusEntity setComponentProps(Map<String,Object> componentProps) {
        this.comProps = JSONUtil.toJsonStr(componentProps);;
        return this;
    }

}
