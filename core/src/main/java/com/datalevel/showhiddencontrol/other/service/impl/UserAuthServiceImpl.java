package com.datalevel.showhiddencontrol.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.service.IBaseApplicationService;
import com.datalevel.showhiddencontrol.base.service.IBaseFunctionModuleService;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.other.dto.UserAuthDto;
import com.datalevel.showhiddencontrol.other.dto.UserAuthSimpleDto;
import com.datalevel.showhiddencontrol.other.dto.UserAuthInfo;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import com.datalevel.showhiddencontrol.other.mapper.UserAuthMapper;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import com.datalevel.showhiddencontrol.other.service.IUserInfoService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    @Autowired
    IBaseApplicationService iBaseApplicationService;
    @Autowired
    IBaseServiceService iBaseServiceService;
    @Autowired
    IAuthFunService iAuthFunService;
    @Autowired
    IBaseFunctionModuleService iBaseFunctionModuleService;

    @Override
    public ResponsePage<UserAuthSimpleDto> getByPage(RequestPage requestPage, UserAuthEntity userAuthQuery) {
//        userAuthEntity.setCreateTime(null).setCreateUser(null);
        Page<UserInfoEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        iUserInfoService.page(page);

        List<Long> userIds = page.getRecords().stream().map(UserInfoEntity::getId).collect(Collectors.toList());
        LambdaQueryWrapper<UserAuthEntity> queryWrapper = new QueryWrapper<UserAuthEntity>().lambda()
                .in(UserAuthEntity::getUserId,userIds);
        List<UserAuthEntity> userAuthEntityList = list(queryWrapper);
        List<Long> authIds = userAuthEntityList.stream().map(UserAuthEntity::getAuthId).collect(Collectors.toList());
        Map<Long, AuthGroupEntity> authGroupEntityMap = iAuthGroupService.list(new QueryWrapper<AuthGroupEntity>()
                        .lambda().in(AuthGroupEntity::getId, authIds)).stream()
                .collect(Collectors.toMap(AuthGroupEntity::getId, Function.identity()));

        Map<Long, List<UserAuthEntity>> userAuthMap  = userAuthEntityList.stream()
                .collect(Collectors.groupingBy(UserAuthEntity::getUserId));

        List<UserAuthSimpleDto> userAuthSimpleDtoList = page.getRecords().stream().map(userInfoEntity -> {
            UserAuthSimpleDto userAuthSimpleDto = BeanUtil.copyProperties(userInfoEntity, UserAuthSimpleDto.class);

            List<String> authNames = userAuthMap.getOrDefault(userInfoEntity.getId(),Lists.newLinkedList())
                    .stream().map(userAuthEntity -> {
                        return authGroupEntityMap.get(userAuthEntity.getAuthId());
                    }).filter(auth -> auth != null)
                    .map(AuthGroupEntity::getAuthName)
                    .collect(Collectors.toList());
            userAuthSimpleDto.setAuthList(authNames);
            return userAuthSimpleDto;
        }).collect(Collectors.toList());

        ResponsePage responsePage = BeanUtil.copyProperties(page, ResponsePage.class);
        return responsePage.setRecords(userAuthSimpleDtoList);
    }

    @Override
    public List<BaseFunctionModuleEntity> getUserFrontendAuth(Long appId, Long userId) {
        List<BaseServiceEntity> baseServiceEntityList = iBaseServiceService.list(new QueryWrapper<BaseServiceEntity>().lambda()
                .eq(BaseServiceEntity::getAppId, appId));
        List<Long> serviceIds = baseServiceEntityList.stream().map(BaseServiceEntity::getId).collect(Collectors.toList());

        List<AuthGroupEntity> authGroupEntityList = iAuthGroupService.selectByUserAndAppServices(userId,appId,serviceIds);
        List<Long> authIds = authGroupEntityList.stream().map(AuthGroupEntity::getId).collect(Collectors.toList());
        List<BaseFunctionModuleEntity> baseFunctionModuleEntities = iBaseFunctionModuleService.selectByAuthId(authIds);
        return baseFunctionModuleEntities;
    }

    @Override
    public UserAuthInfo getUserBackendAuthKeys(String serviceCode, Long userId) {
        BaseServiceEntity serviceEntity = iBaseServiceService.getOne(new QueryWrapper<BaseServiceEntity>().lambda()
                .eq(BaseServiceEntity::getServiceCode, serviceCode));
        List<AuthGroupEntity> authGroupEntityList = iAuthGroupService.selectByUserAndAppServices(userId, serviceEntity.getAppId(), Lists.newArrayList(serviceEntity.getId()));
        //这里往下是两层，1.是连接的功能部分的api,2.是权限key关联的字段 3.具体的表字段不能查看应该设置为null不在查出。
        // TODO: 2021/12/5 第3部分-
        List<Long> authIds = authGroupEntityList.stream().map(AuthGroupEntity::getId).collect(Collectors.toList());
        List<BaseFunctionModuleEntity> baseFunctionModuleEntities = iBaseFunctionModuleService.selectByAuthId(authIds);
        UserAuthInfo userAuthInfo = new UserAuthInfo()
                .setUserId(userId);
        authGroupEntityList.forEach(authGroupEntity -> {
            UserAuthInfo.DataAuth dataAuth = userAuthInfo.getDataAuth();
            if(authGroupEntity.getAppServiceType()== AppServiceEnum.APP){
                dataAuth.getAppKeys().add(authGroupEntity.getAuthCode());
            }else {
                dataAuth.getServiceKeys().add(authGroupEntity.getAuthCode());
            }
        });
        baseFunctionModuleEntities.forEach(functionModuleEntity -> {
            if(StrUtil.isNotBlank(functionModuleEntity.getRequestUrl())){
                userAuthInfo.addApiAuth(functionModuleEntity.getRequestUrl(),functionModuleEntity.getRequestMethod());
            }
        });
        return userAuthInfo;
    }

    @Override
    public List<UserAuthDto> getUserAuth(Long userId) {
        LambdaQueryWrapper<UserAuthEntity> queryWrapper = new QueryWrapper<UserAuthEntity>().lambda()
                .eq(UserAuthEntity::getUserId, userId);
        List<UserAuthEntity> list = list(queryWrapper);
        List<Long> authIds = list.stream().map(UserAuthEntity::getAuthId).collect(Collectors.toList());
        if(authIds.size()==0){
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AuthGroupEntity> queryWrapper1 = new QueryWrapper<AuthGroupEntity>().lambda()
                .in(AuthGroupEntity::getId, authIds);

        Map<Long, AuthGroupEntity> authGroupEntityMap = iAuthGroupService.list(queryWrapper1).stream()
                .collect(Collectors.toMap(AuthGroupEntity::getId, Function.identity()));

        return list.stream().map(authUser->{
                    AuthGroupEntity authGroupEntity = authGroupEntityMap.get(authUser.getAuthId());
                    UserAuthDto userAuthDto = BeanUtil.copyProperties(authGroupEntity, UserAuthDto.class);
                    BeanUtil.copyProperties(authUser, userAuthDto);
                    return userAuthDto;
                }).collect(Collectors.toList());
    }
}
