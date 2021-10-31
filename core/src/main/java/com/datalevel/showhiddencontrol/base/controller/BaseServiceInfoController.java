package com.datalevel.showhiddencontrol.base.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceInfoEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceInfoService;
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
 * 服务表 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Api(tags = "基础服务")
@RestController
@RequestMapping("/base/service-info")
@ApiSort(10)
public class BaseServiceInfoController {
    @Autowired
    IBaseServiceInfoService iBaseServiceInfoService;

    @GetMapping
    @ApiOperation(value = "获取服务")
    public ResponseResult<ResponsePage<BaseServiceInfoEntity>> getByPage(RequestPage requestPage){
        Page<BaseServiceInfoEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        Page page1 = iBaseServiceInfoService.page(page, queryWrapper);
        return new ResponseResult<>(BeanUtil.copyProperties(page,ResponsePage.class));
    }
    @PostMapping
    @ApiOperation(value = "添加服务")
    public ResponseResult<Boolean> addServe(@RequestBody @Validated(Insert.class) BaseServiceInfoEntity request){
        iBaseServiceInfoService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改服务")
    public ResponseResult<Boolean> updateServe(@RequestBody @Validated(Update.class) BaseServiceInfoEntity request){
        iBaseServiceInfoService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除服务")
    public ResponseResult<Boolean> delServe(@RequestBody List<Long> ids){
        iBaseServiceInfoService.removeByIds(ids);
        return new ResponseResult<>(true);
    }
}
