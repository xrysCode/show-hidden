package com.datalevel.showhiddencontrol.base.entity;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能、模块点，按照页面维度划分;	情况1：是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop的key确定唯一，来表示坐标。	情况2：是后端接口，那么也由浏览器地址加api接口确定是否是唯一。
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "base_function_module",autoResultMap = true)
@ApiModel(value="BaseFunctionModuleEntity对象", description="功能、模块点，按照页面维度划分;	情况1：是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop的key确定唯一，来表示坐标。	情况2：是后端接口，那么也由浏览器地址加api接口确定是否是唯一。")
public class BaseFunctionModuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    private Long parentId;

    @NotNull(groups = {Insert.class,Update.class,})
    private Long serviceId;

    @ApiModelProperty(value = "功能名")
    @NotNull(groups = {Insert.class,Update.class,})
    private String funName;

    @ApiModelProperty(value = "功能描述")
    private String funDesc;

    @ApiModelProperty(value = "当前页面对应前端路由，不同的路由对应不同的微服务")
    @NotNull(groups = {Insert.class,Update.class,})
    private String currentReferer;

    @ApiModelProperty(value = "组件唯一标识，（当前页web路由:组件名[:字段标识]）")
    private String comUniqueFlag;

    @ApiModelProperty(value = "后端访问接口")
    private String requestUrl;

    @ApiModelProperty(value = "请求方法")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> requestMethods;

    @ApiModelProperty(value = "页面组件类型json 必有{name,__file,props}")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> componentType;

    @ApiModelProperty(value = "props k-v 对；确定组件props的唯一值，组件是复用的，所以有相同的type，但是组件的props的值不同，这里只保存能够确定组件的关键值来组成K-v的数据，如果权限匹配到这些数据那么就进行管控，确定唯一的方式是referer+type+props的值，当有多个冲突的时候那么增加props的值对")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> componentProps;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private String createUser;

    private LocalDateTime createTime;

}
