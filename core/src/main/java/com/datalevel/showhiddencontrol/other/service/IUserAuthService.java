package com.datalevel.showhiddencontrol.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.other.dto.UserAuthDto;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
public interface IUserAuthService extends IService<UserAuthEntity> {

    ResponsePage<UserAuthDto> getByPage(RequestPage requestPage, UserAuthEntity userAuthEntity);
}
