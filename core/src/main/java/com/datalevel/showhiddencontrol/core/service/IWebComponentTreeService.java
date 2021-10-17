package com.datalevel.showhiddencontrol.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.core.dto.WebTreeDto;
import com.datalevel.showhiddencontrol.core.entity.WebComponentTreeEntity;

import java.util.List;

/**
 * <p>
 * 页面组件完整树 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-05
 */
public interface IWebComponentTreeService extends IService<WebComponentTreeEntity> {

    List<WebTreeDto> getOriginalTree();

    Boolean saveOriginalTree(List<WebTreeDto> webTreeDtoList);
}
