package com.datalevel.showhiddencontrol.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthGroupMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthGroupService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限组，对功能进行分组，用户通过各种关联进入这里，意味着拥有对应的功能。	同时针对数据级的权限，需要对应表加字段保持这个字段来作为数据级的过滤条件。 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, AuthGroupEntity> implements IAuthGroupService {

}
