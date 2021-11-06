package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.ServiceServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseApplicationMapper;
import com.datalevel.showhiddencontrol.base.mapper.BaseFunctionModuleMapper;
import com.datalevel.showhiddencontrol.base.mapper.BaseServiceMapper;
import com.datalevel.showhiddencontrol.base.mapper.BaseTableFieldInfoMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.config.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class BaseServiceServiceImpl extends ServiceImpl<BaseServiceMapper, BaseServiceEntity> implements IBaseServiceService {
    @Autowired
    BaseApplicationMapper baseApplicationMapper;
    @Autowired
    BaseFunctionModuleMapper baseFunctionModuleMapper;
    @Autowired
    BaseTableFieldInfoMapper baseTableFieldInfoMapper;

    @Override
    public ResponsePage<ServiceServerDto> queryPage(RequestPage requestPage, BaseServiceEntity applicationEntityQuery) {
        Page<BaseServiceEntity> page = Page.of(requestPage.getCurrent(), requestPage.getSize());
        LambdaQueryWrapper<BaseServiceEntity> queryWrapper = new QueryWrapper<BaseServiceEntity>().lambda()
                .eq(applicationEntityQuery.getAppId() != null, BaseServiceEntity::getAppId, applicationEntityQuery.getAppId())
                .eq(StrUtil.isNotBlank(applicationEntityQuery.getServiceCode()) , BaseServiceEntity::getServiceCode, applicationEntityQuery.getServiceCode())
                .like(StrUtil.isNotBlank(applicationEntityQuery.getServiceName()) , BaseServiceEntity::getServiceName, applicationEntityQuery.getServiceName());

        baseMapper.selectPage(page, queryWrapper);
        List<BaseServiceEntity> records = page.getRecords();
        List<ServiceServerDto> dtoList = records.stream().map(applicationEntity -> {
            BaseApplicationEntity serviceInfoEntity = baseApplicationMapper.selectById(applicationEntity.getAppId());
            ServiceServerDto applicationServerDto = BeanUtil.copyProperties(serviceInfoEntity, ServiceServerDto.class);
            BeanUtil.copyProperties(applicationEntity,applicationServerDto);
            return applicationServerDto;
        }).collect(Collectors.toList());

        return BeanUtil.copyProperties(records,ResponsePage.class).setRecords(dtoList);
    }

    @Override
    public void delServe(List<Long> ids) {
        HashMap<String, Object> selectMap = new HashMap<>();
        ids.forEach(serviceId->{
            selectMap.put("service_id",serviceId);
            String message = baseFunctionModuleMapper.selectByMap(selectMap).stream().map(BaseFunctionModuleEntity::getFunName)
                    .collect(Collectors.joining(","));
            if(StrUtil.isNotBlank(message)){
                throw new BusinessException(String.format("请先模块功能:[%s]。",message));
            }
            message = baseTableFieldInfoMapper.selectByMap(selectMap).stream().map(BaseTableFieldInfoEntity::getDbTableField)
                    .distinct()
                    .collect(Collectors.joining(","));
            if(StrUtil.isNotBlank(message)){
                throw new BusinessException(String.format("请先数据库表信息:[%s]。",message));
            }
        });
        removeByIds(ids);
    }

}
