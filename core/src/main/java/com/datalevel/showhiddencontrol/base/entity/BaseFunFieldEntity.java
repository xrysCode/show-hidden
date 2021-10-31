package com.datalevel.showhiddencontrol.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 功能模块字段关系 api功能对应的表，主要用在展示页面对页面字段控制提供基础数据
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_fun_field")
@ApiModel(value="BaseFunFieldEntity对象", description="功能模块字段关系 api功能对应的表，主要用在展示页面对页面字段控制提供基础数据")
public class BaseFunFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long fieldId;

    private Long funId;

    private LocalDateTime createTime;

    private String createUser;


}
