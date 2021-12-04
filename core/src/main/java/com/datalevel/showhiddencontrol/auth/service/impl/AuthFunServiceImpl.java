package com.datalevel.showhiddencontrol.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthFunMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseFunctionModuleService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限组与功能关系，多对多 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class AuthFunServiceImpl extends ServiceImpl<AuthFunMapper, AuthFunEntity> implements IAuthFunService {
    @Autowired
    IAuthGroupService iAuthGroupService;
    @Autowired
    IBaseFunctionModuleService iBaseFunctionModuleService;

    @Override
    public ResponsePage<AuthFunDto> getByPage(RequestPage requestPage, AuthFunEntity authFunQuery) {
        authFunQuery.setCreateTime(null).setCreateUser(null);
        Page<AuthFunEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<AuthFunEntity> queryWrapper = new QueryWrapper<AuthFunEntity>().lambda()
                .setEntity(authFunQuery)
                .orderByDesc(AuthFunEntity::getCreateTime);
        page(page, queryWrapper);
        ResponsePage responsePage = BeanUtil.copyProperties(page, ResponsePage.class);
        List<AuthFunDto> authFunDtoList = page.getRecords().stream().map(authFunEntity -> {
            AuthGroupEntity authGroupEntity = iAuthGroupService.getById(authFunEntity.getAuthId());
            BaseFunctionModuleEntity functionModuleEntity = iBaseFunctionModuleService.getById(authFunEntity.getFunId());
            return BeanUtil.copyProperties(authFunEntity, AuthFunDto.class)
                    .setAuthName(authGroupEntity.getAuthName())
                    .setFunName(functionModuleEntity.getFunName());
        }).collect(Collectors.toList());

        return responsePage.setRecords(authFunDtoList);
    }
}
