package com.datalevel.showhiddencontrol.base.controller;


import com.datalevel.showhiddencontrol.base.dto.ApplicationServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseApplicationService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * <p>
 * 微服务粒度 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/base/application")
@ApiSort(20)
@Api(tags = "服务拆分")
public class BaseApplicationController {
    @Autowired
    IBaseApplicationService iBaseApplicationService;

    @GetMapping
    @ApiOperation(value = "获取子服务")
    public ResponseResult<ResponsePage<ApplicationServerDto>> getByPage(RequestPage requestPage, BaseApplicationEntity applicationEntity){
        return new ResponseResult<>(iBaseApplicationService.queryPage( requestPage,  applicationEntity));
    }
    @PostMapping
    @ApiOperation(value = "添加子服务")
    public ResponseResult<Boolean> addServe(@RequestBody @Validated(Insert.class) BaseApplicationEntity request){
        request.setAppCode(UUID.randomUUID().toString());
        iBaseApplicationService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改子服务")
    public ResponseResult<Boolean> updateServe(@RequestBody @Validated(Update.class) BaseApplicationEntity request){
        request.setAppCode(null);
        iBaseApplicationService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除子服务")
    public ResponseResult<Boolean> delServe(@RequestBody BaseApplicationEntity request){
        iBaseApplicationService.removeById(request.getId());
        return new ResponseResult<>(true);
    }
}
