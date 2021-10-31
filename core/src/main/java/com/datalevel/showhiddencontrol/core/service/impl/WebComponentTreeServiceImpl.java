package com.datalevel.showhiddencontrol.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.core.dto.WebTreeDto;
import com.datalevel.showhiddencontrol.core.entity.WebComponentTreeEntity;
import com.datalevel.showhiddencontrol.core.mapper.WebComponentTree;
import com.datalevel.showhiddencontrol.core.service.IWebComponentTreeService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 页面组件完整树 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-05
 */
//@Service
public class WebComponentTreeServiceImpl extends ServiceImpl<WebComponentTree, WebComponentTreeEntity> implements IWebComponentTreeService {

    @Override
    public List<WebTreeDto> getOriginalTree() {
        return fullTree(0);
    }

    private List<WebTreeDto> fullTree(Integer parentId){
        LambdaQueryWrapper<WebComponentTreeEntity> queryWrapper = new QueryWrapper<WebComponentTreeEntity>().lambda()
                .eq(WebComponentTreeEntity::getParentId, parentId)
                .orderByAsc(WebComponentTreeEntity::getSequence);
        List<WebTreeDto> webComponentTreeEntities = baseMapper.selectList(queryWrapper).stream()
                .map(webComponentTreeEntity -> {
                    WebTreeDto webTreeDto = BeanUtil.copyProperties(webComponentTreeEntity, WebTreeDto.class);
                    List<WebTreeDto> webTreeDtos = fullTree(webTreeDto.getId());
                    webTreeDto.setChildNodes(webTreeDtos);
                    return webTreeDto;
                }).collect(Collectors.toList());
        return webComponentTreeEntities;
    }

    @Override
    public Boolean saveOriginalTree(List<WebTreeDto> webTreeDtoList) {
        LinkedList<WebComponentTreeEntity> webComponentTreeEntities = Lists.newLinkedList();
        resolveTree(webTreeDtoList,0,webComponentTreeEntities);
        updateBatchById(webComponentTreeEntities);
        return true;
    }

    public void resolveTree(List<WebTreeDto> webTreeDtoList,Integer parentId,LinkedList<WebComponentTreeEntity> webComponentTreeEntities) {
        for (int i = 0; i < webTreeDtoList.size(); i++) {
            //            BeanUtil.copyProperties()
            WebTreeDto webTreeDto = webTreeDtoList.get(i);
            if(webTreeDto.getId()==null){
                baseMapper.insert(webTreeDto);//获得插入的id
            }
            webComponentTreeEntities.add(webTreeDto);
            webTreeDto.setParentId(parentId).setSequence(i);
            List<WebTreeDto> childNodes = webTreeDto.getChildNodes();
            if(CollectionUtil.isNotEmpty(childNodes)){
                resolveTree(childNodes,webTreeDto.getId(), webComponentTreeEntities);
            }
        }
    }
}
