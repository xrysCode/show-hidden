package com.datalevel.showhiddencontrol.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.other.dto.UserAuthDto;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import com.datalevel.showhiddencontrol.other.mapper.UserAuthMapper;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import com.datalevel.showhiddencontrol.other.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuthEntity> implements IUserAuthService {
    @Autowired
    IUserInfoService iUserInfoService;
    @Autowired
    IAuthGroupService iAuthGroupService;

    @Override
    public ResponsePage<UserAuthDto> getByPage(RequestPage requestPage, UserAuthEntity userAuthQuery) {
//        userAuthEntity.setCreateTime(null).setCreateUser(null);
        Page<UserAuthEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<UserAuthEntity> queryWrapper = new QueryWrapper<UserAuthEntity>().lambda()
                .setEntity(userAuthQuery)
                .orderByDesc(UserAuthEntity::getId);
        page(page, queryWrapper);
        ResponsePage responsePage = BeanUtil.copyProperties(page, ResponsePage.class);
        List<UserAuthDto> userAuthDtoList = page.getRecords().stream().map(userAuthEntity -> {
            AuthGroupEntity authGroupEntity = iAuthGroupService.getById(userAuthEntity.getAuthId());
            UserInfoEntity userInfoEntity = iUserInfoService.getById(userAuthEntity.getUserId());
            return (UserAuthDto)BeanUtil.copyProperties(userInfoEntity, UserAuthDto.class)
                    .setUserId(userInfoEntity.getId())
                    .setAuthId(authGroupEntity.getId())
                    .setAuthName(authGroupEntity.getAuthName())
                    .setId(userAuthEntity.getId());
        }).collect(Collectors.toList());

        return responsePage.setRecords(userAuthDtoList);
    }
}
