package com.datalevel.showhiddencontrol.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.base.entity.BaseFunFieldEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseFunFieldMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseFunFieldService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 功能模块字段关系 api功能对应的表，主要用在展示页面对页面字段控制提供基础数据 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class BaseFunFieldServiceImpl extends ServiceImpl<BaseFunFieldMapper, BaseFunFieldEntity> implements IBaseFunFieldService {

}
