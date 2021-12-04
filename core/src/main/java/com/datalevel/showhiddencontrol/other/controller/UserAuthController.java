package com.datalevel.showhiddencontrol.other.controller;


import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.common.ResponseResult;
import com.datalevel.showhiddencontrol.other.dto.UserAuthDto;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @since 2021-11-07
 */
@RestController
@Api(tags = "用户权限分配")
@RequestMapping("/other/user-auth")
public class UserAuthController {
    @Autowired
    IUserAuthService iUserAuthService;
    @GetMapping
    @ApiOperation(value = "查询用户权限分配")
    public ResponseResult<ResponsePage<UserAuthDto>> getByPage(RequestPage requestPage, UserAuthEntity userAuthEntity){
        return new ResponseResult<>(iUserAuthService.getByPage(requestPage,userAuthEntity));
    }
    @PostMapping
    @ApiOperation(value = "添加用户权限分配")
    public ResponseResult<Boolean> addApp(@RequestBody @Validated(Insert.class) UserAuthEntity request){
        iUserAuthService.save(request);
        return new ResponseResult<>(true);
    }
    @PutMapping
    @ApiOperation(value = "修改用户权限分配")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated(Update.class) UserAuthEntity request){
        iUserAuthService.updateById(request);
        return new ResponseResult<>(true);
    }


    @DeleteMapping
    @ApiOperation(value = "删除用户权限分配")
    public ResponseResult<Boolean> delApp(@RequestBody List<Long> ids){
        iUserAuthService.removeByIds(ids);
        return new ResponseResult<>(true);
    }

}
