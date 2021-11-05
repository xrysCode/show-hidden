package com.datalevel.showhiddencontrol.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;

import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IBaseApplicationService extends IService<BaseApplicationEntity> {

    void delApp(List<Long> ids);
}
