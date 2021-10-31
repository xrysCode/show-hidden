package com.datalevel.showhiddencontrol.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;

/**
 * <p>
 * 功能、模块点，按照页面维度划分;	如果是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop确定唯一，来表示坐标。	如果是后端接口，那么也由浏览器路由加api接口确定是否是唯一。 Mapper 接口
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface BaseFunctionModuleMapper extends BaseMapper<BaseFunctionModuleEntity> {

}
