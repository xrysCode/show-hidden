package com.datalevel.showhiddencontrol.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.dto.ServiceServerDto;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.common.RequestPage;
import com.datalevel.showhiddencontrol.common.ResponsePage;

import java.util.List;

/**
 * <p>
 * 微服务粒度 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IBaseServiceService extends IService<BaseServiceEntity> {

    ResponsePage<ServiceServerDto> queryPage(RequestPage requestPage, BaseServiceEntity applicationEntity);


    void delServe(List<Long> ids);
}
