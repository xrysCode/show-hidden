package com.datalevel.showhiddencontrol.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;

/**
 * <p>
 * 权限组与功能关系，多对多 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IAuthFunService extends IService<AuthFunEntity> {

    ResponsePage<AuthFunDto> getByPage(RequestPage requestPage, AuthFunEntity authFunEntity);
}
