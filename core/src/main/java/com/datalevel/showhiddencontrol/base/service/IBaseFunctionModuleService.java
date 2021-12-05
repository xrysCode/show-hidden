package com.datalevel.showhiddencontrol.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleSaveDto;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleTreeDto;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;

import java.util.List;

/**
 * <p>
 * 功能、模块点，按照页面维度划分;	如果是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop确定唯一，来表示坐标。	如果是后端接口，那么也由浏览器路由加api接口确定是否是唯一。 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IBaseFunctionModuleService extends IService<BaseFunctionModuleEntity> {

    List<FunctionModuleTreeDto> queryByAppId(Long appId);

    void shiftFunctionModule(TreeShiftDto treeShiftDto);

    void delFunctionModule(List<Long> ids);

    void saveRelation(FunctionModuleSaveDto request);

    List<BaseFunctionModuleEntity> selectByAuthId(List<Long> authIds);
}
