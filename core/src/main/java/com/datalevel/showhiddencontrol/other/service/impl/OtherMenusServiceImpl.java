package com.datalevel.showhiddencontrol.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.config.BusinessException;
import com.datalevel.showhiddencontrol.other.dto.MenusTreeDto;
import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;
import com.datalevel.showhiddencontrol.other.mapper.OtherMenusMapper;
import com.datalevel.showhiddencontrol.other.service.IOtherMenusService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-11-03
 */
@Service
public class OtherMenusServiceImpl extends ServiceImpl<OtherMenusMapper, OtherMenusEntity> implements IOtherMenusService {

    @Override
    public List<MenusTreeDto> queryMenusTree() {
        return fullTree(0L);
    }
    private List<MenusTreeDto> fullTree(Long parentId){
        LambdaQueryWrapper<OtherMenusEntity> queryWrapper = new QueryWrapper<OtherMenusEntity>().lambda()
                .eq(OtherMenusEntity::getParentId, parentId)
                .orderByAsc(OtherMenusEntity::getSort);
        List<MenusTreeDto> treeEntities = baseMapper.selectList(queryWrapper).stream()
                .map(menusEntity -> {
                    MenusTreeDto treeDto = BeanUtil.copyProperties(menusEntity, MenusTreeDto.class);
                    List<MenusTreeDto> childrenTrees = fullTree(treeDto.getId());
                    treeDto.setChildren(childrenTrees);
                    return treeDto;
                }).collect(Collectors.toList());
        return treeEntities;
    }
    
    @Override
    @Transactional
    public void shiftMenu(TreeShiftDto treeShiftDto) {
        if(treeShiftDto.getReplaceId().equals(treeShiftDto.getDraggingId())){
            throw new BusinessException("移动的节点相同");
        }
        OtherMenusEntity draggingEntity = baseMapper.selectById(treeShiftDto.getDraggingId());
        OtherMenusEntity replaceEntity = baseMapper.selectById(treeShiftDto.getReplaceId());

        TreeShiftDto.@NotNull DropType dropType = treeShiftDto.getDropType();
        LambdaQueryWrapper<OtherMenusEntity> queryWrapper = new QueryWrapper<OtherMenusEntity>().lambda()
                .orderByAsc(OtherMenusEntity::getSort);
        List<OtherMenusEntity> replaceEntityList =null;
        if(dropType==TreeShiftDto. DropType.inner){
            queryWrapper.eq(OtherMenusEntity::getParentId, replaceEntity.getId());// 内部位于第一个
            replaceEntityList = baseMapper.selectList(queryWrapper);
            draggingEntity.setParentId(replaceEntity.getId()).setSort(0).setCreateTime(null).setCreateUser(null);
        }else {
            queryWrapper=replaceEntity.getParentId()==null?queryWrapper.isNull(OtherMenusEntity::getParentId)
                    :queryWrapper.eq(OtherMenusEntity::getParentId, replaceEntity.getParentId());
            replaceEntityList = baseMapper.selectList(queryWrapper);
            draggingEntity.setParentId(replaceEntity.getParentId()).setCreateTime(null).setCreateUser(null);
        }
        LinkedList<OtherMenusEntity> updateData = Lists.newLinkedList();
        updateData.add(draggingEntity);
        for (int i = 0, replaceIndex=0; i < replaceEntityList.size(); i++,replaceIndex++) {//只需要移动替换节点的排序，移出节点的顺序按原有顺序没有变化。
            OtherMenusEntity moduleEntity = replaceEntityList.get(i);
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
        if(draggingEntity.getParentId()==null){
            LambdaUpdateWrapper<OtherMenusEntity> updateWrapper = new UpdateWrapper<OtherMenusEntity>().lambda();
            updateData.forEach(functionModuleEntity -> {
                updateWrapper.set(OtherMenusEntity::getParentId,null)
                        .eq(OtherMenusEntity::getId,functionModuleEntity.getId());
                update(functionModuleEntity,updateWrapper);
                updateWrapper.clear();
            });
        }else {
            updateBatchById(updateData);
        }
    }
}
