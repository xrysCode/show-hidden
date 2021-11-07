package com.datalevel.showhiddencontrol.base.controller;


import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.apache.ibatis.annotations.Insert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * <p>
 * 功能模块字段关系 api功能对应的表，主要用在展示页面对页面字段控制提供基础数据 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/base/fun-field")
@ApiSort(20)
@Api(tags = "功能表关系维护")
public class BaseFunFieldController {
//    PostMapping
//    @ApiOperation(value = "添加子服务")
//    public ResponseResult<Boolean> addServe(@RequestBody @Validated(Insert.class) BaseServiceEntity request){
//        request.setServiceCode(UUID.randomUUID().toString());
//        iBaseServiceService.save(request);
//        return new ResponseResult<>(true);
//    }
}
