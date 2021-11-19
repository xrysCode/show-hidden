package com.datalevel.showhiddencontrol.base.controller;


import com.datalevel.showhiddencontrol.base.dto.ServiceServerDto;
import com.datalevel.showhiddencontrol.base.dto.TableFieldInfoDto;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseTableFieldInfoService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务的所有字段及表信息 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/base/table-field")
@ApiSort(20)
@Api(tags = "表级所有字段")
public class BaseTableFieldInfoController {
    @Autowired
    IBaseTableFieldInfoService iBaseTableFieldInfoService;

    @GetMapping
    @ApiOperation(value = "获取表级字段",notes = "parentId null表示是表，其他代表字段,")
    public ResponseResult<List<TableFieldInfoDto>> getTableFieldInfo(@RequestParam @ApiParam("服务id") Long serviceId, Long parentId){
        return new ResponseResult<>(iBaseTableFieldInfoService.getTableFieldInfo(  serviceId, parentId));
    }

    @PutMapping
    @ApiOperation(value = "获取表级字段",notes = "parentId null表示是表，其他代表字段,")
    public ResponseResult<Boolean> modifyTableFieldInfo(@RequestBody @Validated BaseTableFieldInfoEntity tableFieldInfoEntity){
        return new ResponseResult<>(iBaseTableFieldInfoService.modifyTableFieldInfo(tableFieldInfoEntity));
    }
}
