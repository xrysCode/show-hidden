package com.datalevel.showhiddencontrol.base.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.dto.ApplicationServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceInfoEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 微服务粒度 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IBaseApplicationService extends IService<BaseApplicationEntity> {

    ResponsePage<ApplicationServerDto> queryPage(RequestPage requestPage, BaseApplicationEntity applicationEntity);


}
