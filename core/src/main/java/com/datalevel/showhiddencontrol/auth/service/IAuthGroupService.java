package com.datalevel.showhiddencontrol.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.auth.dto.AuthGroupDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;

import java.util.List;

/**
 * <p>
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IAuthGroupService extends IService<AuthGroupEntity> {

    void delApp(List<Long> ids);

    ResponsePage<AuthGroupDto> getByPage(RequestPage requestPage);

    List<AuthGroupEntity> selectByUserAndAppServices(Long userId, Long appId, List<Long> serviceIds);
}
