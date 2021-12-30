package com.datalevel.showhiddencontrol.other.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.entity.BaseFunctionModuleEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;
import com.datalevel.showhiddencontrol.other.dto.UserAuthDto;
import com.datalevel.showhiddencontrol.other.dto.UserAuthSimpleDto;
import com.datalevel.showhiddencontrol.other.dto.UserAuthInfo;
import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
public interface IUserAuthService extends IService<UserAuthEntity> {

    ResponsePage<UserAuthSimpleDto> getByPage(RequestPage requestPage, UserAuthEntity userAuthEntity);

    /**
     *
     * @param appId
     * @param userId
     * @return
     */
    List<BaseFunctionModuleEntity> getUserFrontendAuth(Long appId, Long userId);

    /**
     *
     * @param serviceCode
     * @param userId
     * @return
     */
    UserAuthInfo getUserBackendAuthKeys(String serviceCode, Long userId);


    List<UserAuthDto> getUserAuth(Long userId);
}
