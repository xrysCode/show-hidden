package com.datalevel.showhiddencontrol.other.controller;

import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import com.datalevel.showhiddencontrol.other.dto.MenusTreeDto;
import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;
import com.datalevel.showhiddencontrol.other.service.IOtherMenusService;
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
 *  前端控制器
 * </p>
 *
 * @author xry
 * @since 2021-11-03
 */
@RestController
@RequestMapping("/other/menus")
@ApiSort(30)
@Api(tags = "其他非核心模块")
public class OtherMenusController {
    @Autowired
    IOtherMenusService iOtherMenusService;

    @GetMapping
    @ApiOperation(value = "获取菜单树")
    public ResponseResult<List<MenusTreeDto>> getTreeByAppId(){
        return new ResponseResult<>(iOtherMenusService.queryMenusTree());
    }
    @PostMapping
    @ApiOperation(value = "添加菜单")
    public ResponseResult<Boolean> addMenu(@RequestBody @Validated(Insert.class) OtherMenusEntity request){
        iOtherMenusService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改菜单")
    public ResponseResult<Boolean> updateMenu(@RequestBody @Validated(Update.class) OtherMenusEntity request){
        iOtherMenusService.updateById(request);
        return new ResponseResult<>(true);
    }

    @PutMapping("/shift")
    @ApiOperation(value = "移动菜单")
    public ResponseResult<Boolean> shiftMenu(@RequestBody @Validated TreeShiftDto treeShiftDto){
        iOtherMenusService.shiftMenu(treeShiftDto);
        return new ResponseResult<>(true);
    }

    @DeleteMapping
    @ApiOperation(value = "删除选中的节点")
    public ResponseResult<Boolean> delMenu(@RequestBody List<Long> ids){
        iOtherMenusService.removeByIds(ids);
        return new ResponseResult<>(true);
    }
}
