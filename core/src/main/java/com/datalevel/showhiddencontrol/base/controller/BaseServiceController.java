package com.datalevel.showhiddencontrol.base.controller;


import com.datalevel.showhiddencontrol.base.dto.ServiceServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceService;
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

import java.util.List;
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
@RequestMapping("/base/service")
@ApiSort(20)
@Api(tags = "服务拆分")
public class BaseServiceController {
    @Autowired
    IBaseServiceService iBaseServiceService;

    @GetMapping
    @ApiOperation(value = "获取子服务")
    public ResponseResult<ResponsePage<ServiceServerDto>> getByPage(RequestPage requestPage, BaseServiceEntity applicationEntity){
        return new ResponseResult<>(iBaseServiceService.queryPage( requestPage,  applicationEntity));
    }
    @PostMapping
    @ApiOperation(value = "添加子服务")
    public ResponseResult<Boolean> addServe(@RequestBody @Validated(Insert.class) BaseServiceEntity request){
        request.setServiceCode(UUID.randomUUID().toString());
        iBaseServiceService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改子服务")
    public ResponseResult<Boolean> updateServe(@RequestBody @Validated(Update.class) BaseServiceEntity request){
        request.setServiceCode(null);
        iBaseServiceService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除子服务")
    public ResponseResult<Boolean> delServe(@RequestBody List<Long> ids){
        iBaseServiceService.delServe(ids);
        return new ResponseResult<>(true);
    }
}
