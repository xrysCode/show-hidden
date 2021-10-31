package com.datalevel.showhiddencontrol.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.entity.AuthFieldEntity;
import com.datalevel.showhiddencontrol.auth.mapper.AuthFieldMapper;
import com.datalevel.showhiddencontrol.auth.service.IAuthFieldService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字段控制，不同的权限组需要不同的字段 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class AuthFieldServiceImpl extends ServiceImpl<AuthFieldMapper, AuthFieldEntity> implements IAuthFieldService {

}
