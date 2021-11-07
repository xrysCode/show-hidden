package com.datalevel.showhiddencontrol.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    @Autowired


}
