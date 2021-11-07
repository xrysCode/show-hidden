package com.datalevel.showhiddencontrol.other.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import com.datalevel.showhiddencontrol.other.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
@RestController
@RequestMapping("/other/user-info")
@ApiSort(10)
@Api(tags = "用户")
public class UserInfoController {
    @Autowired
    IUserInfoService iUserInfoService;

    @GetMapping
    @ApiOperation(value = "获取用户")
    public ResponseResult<ResponsePage<UserInfoEntity>> getByPage(RequestPage requestPage){
        Page<UserInfoEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<UserInfoEntity>().lambda().orderByAsc(UserInfoEntity::getId);
        iUserInfoService.page(page, queryWrapper);
        return new ResponseResult<>(BeanUtil.copyProperties(page,ResponsePage.class));
    }
}
