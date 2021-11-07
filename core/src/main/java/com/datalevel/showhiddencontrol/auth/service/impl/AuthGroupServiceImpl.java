package com.datalevel.showhiddencontrol.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.entity.AuthFieldEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthGroupMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthFieldService;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
