package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleTreeDto;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseFunctionModuleMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseFunctionModuleService;
import com.datalevel.showhiddencontrol.core.dto.WebTreeDto;
import com.datalevel.showhiddencontrol.core.entity.WebComponentTreeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能、模块点，按照页面维度划分;	如果是web 那么是组件，组件由第一次访问页面的路径+组件自己的prop确定唯一，来表示坐标。	如果是后端接口，那么也由浏览器路由加api接口确定是否是唯一。 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class BaseFunctionModuleServiceImpl extends ServiceImpl<BaseFunctionModuleMapper, BaseFunctionModuleEntity> implements IBaseFunctionModuleService {

    @Override
    public List<FunctionModuleTreeDto> queryByAppId(Long appId) {
        return fullTree( appId, 0L);
    }

    private List<FunctionModuleTreeDto> fullTree(Long appId,Long parentId){
        LambdaQueryWrapper<BaseFunctionModuleEntity> queryWrapper = new QueryWrapper<BaseFunctionModuleEntity>().lambda()
                .eq(BaseFunctionModuleEntity::getAppId,appId)
                .eq(BaseFunctionModuleEntity::getParentId, parentId)
                .orderByAsc(BaseFunctionModuleEntity::getSort);
        List<FunctionModuleTreeDto> treeEntities = baseMapper.selectList(queryWrapper).stream()
                .map(functionModuleEntity -> {
                    FunctionModuleTreeDto treeDto = BeanUtil.copyProperties(functionModuleEntity, FunctionModuleTreeDto.class);
                    List<FunctionModuleTreeDto> childrenTrees = fullTree(appId,treeDto.getId());
                    treeDto.setChildren(childrenTrees);
                    return treeDto;
                }).collect(Collectors.toList());
        return treeEntities;
    }

    @Override
    @Transactional
    public void shiftFunctionModule(TreeShiftDto treeShiftDto) {
        BaseFunctionModuleEntity draggingEntity = baseMapper.selectById(treeShiftDto.getDraggingId());
        BaseFunctionModuleEntity replaceEntity = baseMapper.selectById(treeShiftDto.getReplaceId());

        LambdaQueryWrapper<BaseFunctionModuleEntity> queryWrapper = new QueryWrapper<BaseFunctionModuleEntity>().lambda()
                .eq(BaseFunctionModuleEntity::getParentId, replaceEntity.getParentId())
                .orderByAsc(BaseFunctionModuleEntity::getSort);
        List<BaseFunctionModuleEntity> replaceEntityList = baseMapper.selectList(queryWrapper);

        int replaceIndex=0;
        for (int i = 0; i < replaceEntityList.size(); i++,replaceIndex++) {//只需要移动替换节点的排序，移出节点的顺序按原有顺序没有变化。
            BaseFunctionModuleEntity moduleEntity = replaceEntityList.get(i);
            if(moduleEntity.getId().equals(treeShiftDto.getReplaceId())){
                draggingEntity.setSort(replaceIndex).setParentId(replaceEntity.getParentId()).setCreateTime(null).setCreateUser(null);
                replaceIndex++;
            }
            moduleEntity.setSort(replaceIndex).setCreateTime(null).setCreateUser(null);
        }
        replaceEntityList.add(draggingEntity.getSort(),draggingEntity);
        updateBatchById(replaceEntityList);
    }
}
