package com.datalevel.showhiddencontrol.auth.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
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
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/auth/group")
@ApiSort(10)
@Api(tags = "功能分组权限")
public class AuthGroupController {
    @Autowired
    IAuthGroupService iAuthGroupService;

    @GetMapping
    @ApiOperation(value = "查询分组")
    public ResponseResult<ResponsePage<AuthGroupEntity>> getByPage(RequestPage requestPage){
        Page<AuthGroupEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<AuthGroupEntity> queryWrapper = new QueryWrapper<AuthGroupEntity>()
                .lambda().orderByDesc(AuthGroupEntity::getCreateTime);
        iAuthGroupService.page(page, queryWrapper);
        return new ResponseResult<>(BeanUtil.copyProperties(page,ResponsePage.class));
    }
    @PostMapping
    @ApiOperation(value = "添加权限")
    public ResponseResult<Boolean> addApp(@RequestBody @Validated(Insert.class) AuthGroupEntity request){
        iAuthGroupService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改权限")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated(Update.class) AuthGroupEntity request){
        iAuthGroupService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除分组")
    public ResponseResult<Boolean> delApp(@RequestBody List<Long> ids){
        iAuthGroupService.delApp(ids);
        return new ResponseResult<>(true);
    }


}
