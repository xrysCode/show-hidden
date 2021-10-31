package com.datalevel.showhiddencontrol.core.controller;


import com.datalevel.showhiddencontrol.common.ResponseResult;
import com.datalevel.showhiddencontrol.core.dto.WebTreeDto;
import com.datalevel.showhiddencontrol.core.service.IWebComponentTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 页面组件完整树 前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-10-05
 */
//@RestController
@RequestMapping("/web/component-tree")
//@ApiOperation("web组件树")
@Api(tags = "组件树")
//@ApiSort(value = 200)
public class WebComponentTreeController {

    @Autowired
    IWebComponentTreeService iWebComponentTreeService;
    @GetMapping
    @ApiOperation(value = "获取组件树")
    public ResponseResult<List<WebTreeDto>> getTree(){
        return new ResponseResult<>(iWebComponentTreeService.getOriginalTree());
    }

    @PostMapping
    @ApiOperation(value = "保存修改组件树")
    public ResponseResult<Boolean> saveTree(@RequestBody List<WebTreeDto> webTreeDtoList){
        return new ResponseResult<>(iWebComponentTreeService.saveOriginalTree(webTreeDtoList));
    }
}
