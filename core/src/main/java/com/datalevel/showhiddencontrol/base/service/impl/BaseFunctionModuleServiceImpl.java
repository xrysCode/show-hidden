package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.mapper.AuthFieldMapper;
import com.datalevel.showhiddencontrol.auth.mapper.AuthFunMapper;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleSaveDto;
import com.datalevel.showhiddencontrol.base.dto.FunctionModuleTreeDto;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunFieldEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseFunFieldMapper;
import com.datalevel.showhiddencontrol.base.mapper.BaseFunctionModuleMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseFunFieldService;
import com.datalevel.showhiddencontrol.base.service.IBaseFunctionModuleService;
import com.datalevel.showhiddencontrol.config.BusinessException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    AuthFunMapper authFunMapper;
    @Autowired
    IBaseFunFieldService iBaseFunFieldService;
    @Autowired
    AuthFieldMapper authFieldMapper;


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
        if(treeShiftDto.getReplaceId().equals(treeShiftDto.getDraggingId())){
            throw new BusinessException("移动的节点相同");
        }
        BaseFunctionModuleEntity draggingEntity = baseMapper.selectById(treeShiftDto.getDraggingId());
        BaseFunctionModuleEntity replaceEntity = baseMapper.selectById(treeShiftDto.getReplaceId());


        TreeShiftDto.@NotNull DropType dropType = treeShiftDto.getDropType();
        LambdaQueryWrapper<BaseFunctionModuleEntity> queryWrapper = new QueryWrapper<BaseFunctionModuleEntity>().lambda()
                .orderByAsc(BaseFunctionModuleEntity::getSort);
        List<BaseFunctionModuleEntity> replaceEntityList =null;
        if(dropType==TreeShiftDto. DropType.inner){
            queryWrapper.eq(BaseFunctionModuleEntity::getParentId, replaceEntity.getId());// 内部位于第一个
            replaceEntityList = baseMapper.selectList(queryWrapper);
            draggingEntity.setParentId(replaceEntity.getId()).setSort(0).setCreateTime(null).setCreateUser(null);
        }else {
            queryWrapper.eq(BaseFunctionModuleEntity::getParentId, replaceEntity.getParentId());
            replaceEntityList = baseMapper.selectList(queryWrapper);
            draggingEntity.setParentId(replaceEntity.getParentId()).setCreateTime(null).setCreateUser(null);
        }
        LinkedList<BaseFunctionModuleEntity> updateData = Lists.newLinkedList();
        updateData.add(draggingEntity);
        for (int i = 0, replaceIndex=0; i < replaceEntityList.size(); i++,replaceIndex++) {//只需要移动替换节点的排序，移出节点的顺序按原有顺序没有变化。
            BaseFunctionModuleEntity moduleEntity = replaceEntityList.get(i);
            switch (dropType){
                case inner:
                    moduleEntity.setSort(replaceIndex+1).setCreateTime(null).setCreateUser(null);
                    break;
                case before://之前替代本节点
                    if(moduleEntity.getId().equals(treeShiftDto.getReplaceId())) {//替换的节点
                        draggingEntity.setSort(replaceIndex);
                        moduleEntity.setSort(++replaceIndex).setCreateTime(null).setCreateUser(null);
                    }else {
                        moduleEntity.setSort(replaceIndex).setCreateTime(null).setCreateUser(null);
                    }
                    break;
                case after:
                    if(moduleEntity.getId().equals(treeShiftDto.getReplaceId())) {//替换的节点
                        moduleEntity.setSort(replaceIndex).setCreateTime(null).setCreateUser(null);
                        draggingEntity.setSort(++replaceIndex);//其他节点要往后移
                    }else {
                        moduleEntity.setSort(replaceIndex).setCreateTime(null).setCreateUser(null);
                    }
                    break;
            }
            if(moduleEntity.getId()!=draggingEntity.getId()){// 同级中不能包含拖拽的节点 因为已经添加了
                updateData.add(moduleEntity);// 全量更新因为可能不连续
            }
        }
        updateBatchById(replaceEntityList);

    }

    @Override
    @Transactional
    public void delFunctionModule(List<Long> ids) {
        Map<String, Object> selectMap = new HashMap<>();
        ids.stream().forEach(funId->{
            selectMap.put("fun_id",funId);
            iBaseFunFieldService.removeByMap(selectMap);
            authFunMapper.deleteByMap(selectMap);
            authFieldMapper.deleteByMap(selectMap);
        });
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void saveRelation(FunctionModuleSaveDto functionModuleSaveDto) {
        if(functionModuleSaveDto.getId()==null){
            save(functionModuleSaveDto);
        }else{
            updateById(functionModuleSaveDto);
        }
        LambdaQueryWrapper<BaseFunFieldEntity> queryWrapper = new QueryWrapper<BaseFunFieldEntity>().lambda()
                .eq(BaseFunFieldEntity::getFunId, functionModuleSaveDto.getId());
        iBaseFunFieldService.remove(queryWrapper);
        if(CollectionUtil.isNotEmpty(functionModuleSaveDto.getFieldIds())){

            List<BaseFunFieldEntity> baseFunFieldEntityList = functionModuleSaveDto.getFieldIds().stream().map(fieldId ->
                    new BaseFunFieldEntity().setFunId(functionModuleSaveDto.getId())
                            .setFieldId(fieldId)
            ).collect(Collectors.toList());
            iBaseFunFieldService.saveBatch(baseFunFieldEntityList);
        }
    }
}
