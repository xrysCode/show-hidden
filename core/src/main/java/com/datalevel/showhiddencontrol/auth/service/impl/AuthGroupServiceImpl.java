package com.datalevel.showhiddencontrol.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import com.datalevel.showhiddencontrol.auth.dto.AuthGroupDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFieldEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthGroupMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthFieldService;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseApplicationService;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.Unsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, AuthGroupEntity> implements IAuthGroupService {
    @Autowired
    IAuthFunService iAuthFunService;
    @Autowired
    IAuthFieldService iAuthFieldService;
    @Autowired
    IUserAuthService iUserAuthService;
    @Autowired
    IBaseApplicationService iBaseApplicationService;
    @Autowired
    IBaseServiceService iBaseServiceService;

    @Override
    public ResponsePage<AuthGroupDto> getByPage(RequestPage requestPage) {
        Page<AuthGroupEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<AuthGroupEntity> queryWrapper = new QueryWrapper<AuthGroupEntity>()
                .lambda().orderByDesc(AuthGroupEntity::getCreateTime);
        page(page, queryWrapper);
        List<AuthGroupDto> authGroupDtoList = page.getRecords().stream().map(authGroupEntity -> {
            AuthGroupDto authGroupDto = BeanUtil.copyProperties(authGroupEntity, AuthGroupDto.class);
            if (authGroupEntity.getAppServiceType() == AppServiceEnum.APP) {
                BaseApplicationEntity applicationEntity = iBaseApplicationService.getById(authGroupEntity.getAppServiceId());
                authGroupDto.setAppServiceName(applicationEntity.getAppName());
            } else {
                BaseServiceEntity serviceEntity = iBaseServiceService.getById(authGroupEntity.getAppServiceId());
                authGroupDto.setAppServiceName(serviceEntity.getServiceName());
            }
            return authGroupDto;
        }).collect(Collectors.toList());

        ResponsePage responsePage = BeanUtil.copyProperties(requestPage, ResponsePage.class);
        return responsePage.setTotal(page.getTotal())
                .setRecords(authGroupDtoList);
    }

    @Override
    @Transactional
    public void delApp(List<Long> ids) {
        LambdaQueryWrapper<AuthFunEntity> queryAuthFunWrapper = new QueryWrapper<AuthFunEntity>().lambda()
                .in(AuthFunEntity::getAuthId, ids);
        iAuthFunService.remove(queryAuthFunWrapper);

        LambdaQueryWrapper<AuthFieldEntity> queryAuthFieldWrapper = new QueryWrapper<AuthFieldEntity>().lambda()
                .in(AuthFieldEntity::getAuthId, ids);
        iAuthFieldService.remove(queryAuthFieldWrapper);

        LambdaQueryWrapper<UserAuthEntity> queryUserAuthWrapper = new QueryWrapper<UserAuthEntity>().lambda()
                .in(UserAuthEntity::getAuthId, ids);
        iUserAuthService.remove(queryUserAuthWrapper);

        removeByIds(ids);
    }

    @Override
    public List<AuthGroupEntity> selectByUserAndAppServices(Long userId, Long appId, List<Long> serviceIds) {
        return getBaseMapper().selectByUserAndAppServices( userId,  appId, serviceIds);
    }

    @Override
    public List<AuthGroupEntity> getAuthOptions(AppServiceEnum appServiceType, Long serviceId) {
        Long appServiceId = serviceId;
        if(appServiceType==AppServiceEnum.APP){
            appServiceId = iBaseServiceService.getById(serviceId).getAppId();
        }
        LambdaQueryWrapper<AuthGroupEntity> queryWrapper = new QueryWrapper<AuthGroupEntity>().lambda()
                .eq(AuthGroupEntity::getAppServiceType, appServiceType)
                .eq(AuthGroupEntity::getAppServiceId, appServiceId);
        return list(queryWrapper);
    }
}
