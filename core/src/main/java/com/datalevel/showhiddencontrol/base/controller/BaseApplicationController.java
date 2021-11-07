package com.datalevel.showhiddencontrol.base.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

import java.util.List;

/**
 * <p>
 * 服务表 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Api(tags = "应用")
@RestController
@RequestMapping("/base/application")
@ApiSort(10)
public class BaseApplicationController {
    @Autowired
    IBaseApplicationService iBaseApplicationService;

    @GetMapping
    @ApiOperation(value = "获取应用")
    public ResponseResult<ResponsePage<BaseApplicationEntity>> getByPage(RequestPage requestPage){
        Page<BaseApplicationEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("create_time");
        Page page1 = iBaseApplicationService.page(page, queryWrapper);
        return new ResponseResult<>(BeanUtil.copyProperties(page,ResponsePage.class));
    }
    @PostMapping
    @ApiOperation(value = "添加应用")
    public ResponseResult<Boolean> addApp(@RequestBody @Validated(Insert.class) BaseApplicationEntity request){
        iBaseApplicationService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改应用")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated(Update.class) BaseApplicationEntity request){
        iBaseApplicationService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除应用")
    public ResponseResult<Boolean> delApp(@RequestBody List<Long> ids){
        iBaseApplicationService.delApp(ids);
        return new ResponseResult<>(true);
    }
}
