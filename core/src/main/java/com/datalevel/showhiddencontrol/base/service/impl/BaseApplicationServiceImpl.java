package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.ApplicationServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceInfoEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseApplicationMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseApplicationService;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceInfoService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 微服务粒度 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class BaseApplicationServiceImpl extends ServiceImpl<BaseApplicationMapper, BaseApplicationEntity> implements IBaseApplicationService {
    @Autowired
    IBaseServiceInfoService iBaseServiceInfoService;

    @Override
    public ResponsePage<ApplicationServerDto> queryPage(RequestPage requestPage, BaseApplicationEntity applicationEntityQuery) {
        Page<BaseApplicationEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<BaseApplicationEntity> queryWrapper = new QueryWrapper<BaseApplicationEntity>().lambda()
                .eq(applicationEntityQuery.getServiceId() != null, BaseApplicationEntity::getServiceId, applicationEntityQuery.getServiceId())
                .eq(StrUtil.isNotBlank(applicationEntityQuery.getAppCode()) , BaseApplicationEntity::getAppCode, applicationEntityQuery.getAppCode())
                .like(StrUtil.isNotBlank(applicationEntityQuery.getAppName()) , BaseApplicationEntity::getAppName, applicationEntityQuery.getAppName());

        baseMapper.selectPage(page, queryWrapper);
        List<BaseApplicationEntity> records = page.getRecords();
        List<ApplicationServerDto> dtoList = records.stream().map(applicationEntity -> {
            BaseServiceInfoEntity serviceInfoEntity = iBaseServiceInfoService.getById(applicationEntity.getServiceId());
            ApplicationServerDto applicationServerDto = BeanUtil.copyProperties(serviceInfoEntity, ApplicationServerDto.class);
            BeanUtil.copyProperties(applicationEntity,applicationServerDto);
            return applicationServerDto;
        }).collect(Collectors.toList());

        return BeanUtil.copyProperties(records,ResponsePage.class).setRecords(dtoList);
    }

}
