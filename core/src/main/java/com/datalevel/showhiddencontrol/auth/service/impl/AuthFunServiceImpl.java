package com.datalevel.showhiddencontrol.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthFunMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthFunService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限组与功能关系，多对多 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class AuthFunServiceImpl extends ServiceImpl<AuthFunMapper, AuthFunEntity> implements IAuthFunService {

}
