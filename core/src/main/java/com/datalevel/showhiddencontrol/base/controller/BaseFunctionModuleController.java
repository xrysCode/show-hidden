package com.datalevel.showhiddencontrol.base.controller;


import com.datalevel.showhiddencontrol.base.dto.FunctionModuleSaveDto;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleTreeDto;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseFunctionModuleService;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 功能、模块点，按照页面维度划分;	如果是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop确定唯一，来表示坐标。	如果是后端接口，那么也由浏览器路由加api接口确定是否是唯一。 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/base/function-module")
@ApiSort(30)
@Api(tags = "功能模块树")
public class BaseFunctionModuleController {
    @Autowired
    IBaseFunctionModuleService iBaseFunctionModuleService;

    @GetMapping
    @ApiOperation(value = "获取服务的功能树")
    public ResponseResult<List<FunctionModuleTreeDto>> getTreeByAppId(@RequestParam Long appId){
        return new ResponseResult<>(iBaseFunctionModuleService.queryByAppId(appId));
    }
    @PostMapping
    @ApiOperation(value = "添加功能模块")
    public ResponseResult<Boolean> addFunctionModule(@RequestBody @Validated(Insert.class) FunctionModuleSaveDto request){
        iBaseFunctionModuleService.saveRelation(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改功能模块")
    public ResponseResult<Boolean> updateFunctionModule(@RequestBody @Validated(Update.class) FunctionModuleSaveDto request){
        iBaseFunctionModuleService.saveRelation(request);
        return new ResponseResult<>(true);
    }

    @PutMapping("/shift")
    @ApiOperation(value = "移动功能模块")
    public ResponseResult<Boolean> shiftFunctionModule(@RequestBody @Validated TreeShiftDto treeShiftDto){
        iBaseFunctionModuleService.shiftFunctionModule(treeShiftDto);
        return new ResponseResult<>(true);
    }

    @DeleteMapping
    @ApiOperation(value = "删除选中的节点")
    public ResponseResult<Boolean> delFunctionModule(@RequestBody List<Long> ids){
        iBaseFunctionModuleService.delFunctionModule(ids);
        return new ResponseResult<>(true);
    }
}
