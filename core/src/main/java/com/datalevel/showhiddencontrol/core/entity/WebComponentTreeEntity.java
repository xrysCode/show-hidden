package com.datalevel.showhiddencontrol.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 页面组件完整树
 * </p>
 *
 * @author xry
 * @since 2021-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_web_component_tree")
@ApiModel(value="WebComponentTreeEntity对象", description="页面组件完整树")
public class WebComponentTreeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "组件实例id 仅参考")
    private Integer uid;

    @ApiModelProperty(value = "组件名")
    private String name;

    @ApiModelProperty(value = "组件文件路径")
    private String filePath;

    @ApiModelProperty(value = "组件类型标记")
    private Integer shapeFlag;

    @ApiModelProperty(value = "组件参数attribute（json）")
    private String props;

    @ApiModelProperty(value = "数组的排序")
    private Integer sequence;

    @ApiModelProperty(value = "父组件id，顶层为0")
    private Integer parentId;

    @ApiModelProperty(value = "是否需要权限控制")
    private Boolean needAuth;


}
