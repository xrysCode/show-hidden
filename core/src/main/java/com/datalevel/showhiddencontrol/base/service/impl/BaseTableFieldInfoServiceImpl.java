package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.TableFieldInfoDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunFieldEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseTableFieldInfoMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseFunFieldService;
import com.datalevel.showhiddencontrol.base.service.IBaseTableFieldInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 服务的所有字段及表信息 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class BaseTableFieldInfoServiceImpl extends ServiceImpl<BaseTableFieldInfoMapper, BaseTableFieldInfoEntity> implements IBaseTableFieldInfoService {
    @Autowired
    IBaseFunFieldService iBaseFunFieldService;


    @Override
    public List<TableFieldInfoDto> getTableFieldInfo(Long serviceId, Long parentId) {
        LambdaQueryWrapper<BaseTableFieldInfoEntity> queryWrapper = new QueryWrapper<BaseTableFieldInfoEntity>().lambda()
                .eq(BaseTableFieldInfoEntity::getServiceId, serviceId)
                .orderByAsc(BaseTableFieldInfoEntity::getDbTableField);
        if(parentId==null){
            queryWrapper.isNull(BaseTableFieldInfoEntity::getParentId);
        }else{
            queryWrapper.eq(BaseTableFieldInfoEntity::getParentId,parentId);
        }

        List<BaseTableFieldInfoEntity> fieldInfoEntityList = baseMapper.selectList(queryWrapper);
        List<Long> tableFieldIds = fieldInfoEntityList.stream().map(BaseTableFieldInfoEntity::getId).collect(Collectors.toList());

        LambdaQueryWrapper<BaseFunFieldEntity> queryFunFieldWrapper = new QueryWrapper<BaseFunFieldEntity>().lambda()
                .in(BaseFunFieldEntity::getFieldId,tableFieldIds);
        List<Long> fieldIds = iBaseFunFieldService.getBaseMapper().selectList(queryFunFieldWrapper)
                .stream().map(BaseFunFieldEntity::getFieldId)
                .collect(Collectors.toList());

        return fieldInfoEntityList.stream().map(fieldInfoEntity->
            BeanUtil.copyProperties(fieldInfoEntity,TableFieldInfoDto.class)
            .setIsSelected(fieldIds.contains(fieldInfoEntity.getId()))
        ).collect(Collectors.toList());
    }

    @Override
    public Boolean modifyTableFieldInfo(BaseTableFieldInfoEntity tableFieldInfoEntity) {
        tableFieldInfoEntity.setParentId(null)
                .setServiceId(null)
                .setDbTableField(null)
                .setPropertyName(null);
        return updateById(tableFieldInfoEntity);
    }

}
