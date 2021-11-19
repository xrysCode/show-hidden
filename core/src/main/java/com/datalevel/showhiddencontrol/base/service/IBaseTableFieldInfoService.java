package com.datalevel.showhiddencontrol.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datalevel.showhiddencontrol.base.dto.TableFieldInfoDto;
import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;

import java.util.List;

/**
 * <p>
 * 服务的所有字段及表信息 服务类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
public interface IBaseTableFieldInfoService extends IService<BaseTableFieldInfoEntity> {

    List<TableFieldInfoDto> getTableFieldInfo(Long serviceId, Long parentId);

    Boolean modifyTableFieldInfo(BaseTableFieldInfoEntity tableFieldInfoEntity);
}
