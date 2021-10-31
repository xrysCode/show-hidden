package com.datalevel.showhiddencontrol.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datalevel.showhiddencontrol.auth.entity.AuthFieldEntity;

/**
 * <p>
 * 字段控制，不同的权限组需要不同的字段 Mapper 接口
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface AuthFieldMapper extends BaseMapper<AuthFieldEntity> {

}
