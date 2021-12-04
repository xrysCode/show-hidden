package com.datalevel.showhiddencontrol.auth.controller;


import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
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

/**
 * <p>
 * 权限组与功能关系，多对多 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/auth/fun")
@ApiSort(10)
@Api(tags = "功能分组")
public class AuthFunController {
    @Autowired
    IAuthFunService iAuthFunService;
    @GetMapping
    @ApiOperation(value = "查询权限功能")
    public ResponseResult<ResponsePage<AuthFunDto>> getByPage(RequestPage requestPage, AuthFunEntity authFunEntity){
        return new ResponseResult<>(iAuthFunService.getByPage(requestPage,authFunEntity));
    }
    @PostMapping
    @ApiOperation(value = "添加权限功能")
    public ResponseResult<Boolean> addApp(@RequestBody @Validated(Insert.class) AuthFunEntity request){
        iAuthFunService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改权限功能",notes = "不允许修改归属服务")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated(Update.class) AuthFunEntity request){
        iAuthFunService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除权限功能")
    public ResponseResult<Boolean> delApp(@RequestBody List<Long> ids){
        iAuthFunService.removeByIds(ids);
        return new ResponseResult<>(true);
    }

}
