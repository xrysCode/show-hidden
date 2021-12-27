package com.datalevel.showhiddencontrol.auth.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import com.datalevel.showhiddencontrol.auth.dto.AuthGroupDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/auth/group")
@ApiSort(10)
@Api(tags = "权限组")
public class AuthGroupController {
    @Autowired
    IAuthGroupService iAuthGroupService;

    @GetMapping
    @ApiOperation(value = "查询分组")
    public ResponseResult<ResponsePage<AuthGroupDto>> getByPage(RequestPage requestPage){
        return new ResponseResult<>(iAuthGroupService.getByPage(requestPage));
    }
    @PostMapping
    @ApiOperation(value = "添加权限")
    public ResponseResult<Boolean> addApp(@RequestBody @Validated(Insert.class) AuthGroupEntity request){
        LambdaQueryWrapper<AuthGroupEntity> queryWrapper = new QueryWrapper<AuthGroupEntity>().lambda()
                .eq(AuthGroupEntity::getAppServiceType,request.getAppServiceType())
                .eq(AuthGroupEntity::getAppServiceId, request.getAppServiceId());
        List<AuthGroupEntity> list = iAuthGroupService.list(queryWrapper);
        request.setAuthCode(list.size()+1);
        iAuthGroupService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改权限",notes = "不允许修改归属服务")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated(Update.class) AuthGroupEntity request){
        request.setAppServiceType(null)
                .setAppServiceId(null);
        iAuthGroupService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除分组")
    public ResponseResult<Boolean> delApp(@RequestBody List<Long> ids){
        iAuthGroupService.delApp(ids);
        return new ResponseResult<>(true);
    }

    @GetMapping("getAuthOptions")
    @ApiOperation(value = "查询下拉权限选项")
    public ResponseResult<List<AuthGroupEntity>> getAuthOptions(@RequestParam AppServiceEnum appServiceType,@RequestParam Long appServiceId){
        return new ResponseResult<>(iAuthGroupService.getAuthOptions(appServiceType,appServiceId));
    }
}
