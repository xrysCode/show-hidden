package com.datalevel.showhiddencontrol.other.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.dto.TreeShiftDto;
import com.datalevel.showhiddencontrol.other.dto.MenusTreeDto;
import com.datalevel.showhiddencontrol.other.entity.OtherMenusEntity;
import com.datalevel.showhiddencontrol.other.mapper.OtherMenusMapper;
import com.datalevel.showhiddencontrol.other.service.IOtherMenusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        OtherMenusEntity draggingEntity = baseMapper.selectById(treeShiftDto.getDraggingId());
        OtherMenusEntity replaceEntity = baseMapper.selectById(treeShiftDto.getReplaceId());

        LambdaQueryWrapper<OtherMenusEntity> queryWrapper = new QueryWrapper<OtherMenusEntity>().lambda()
                .eq(OtherMenusEntity::getParentId, replaceEntity.getParentId())
                .orderByAsc(OtherMenusEntity::getSort);
        List<OtherMenusEntity> replaceEntityList = baseMapper.selectList(queryWrapper);

        int replaceIndex=0;
        for (int i = 0; i < replaceEntityList.size(); i++,replaceIndex++) {//只需要移动替换节点的排序，移出节点的顺序按原有顺序没有变化。
            OtherMenusEntity moduleEntity = replaceEntityList.get(i);
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
