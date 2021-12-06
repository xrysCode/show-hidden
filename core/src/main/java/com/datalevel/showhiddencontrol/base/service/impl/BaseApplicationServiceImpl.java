package com.datalevel.showhiddencontrol.base.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import com.datalevel.showhiddencontrol.base.dto.CascaderAppServiceDto;
import com.datalevel.showhiddencontrol.base.entity.BaseApplicationEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseApplicationMapper;
import com.datalevel.showhiddencontrol.base.mapper.BaseServiceMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseApplicationService;
import com.datalevel.showhiddencontrol.base.service.IBaseServiceService;
import com.datalevel.showhiddencontrol.config.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Service
public class BaseApplicationServiceImpl extends ServiceImpl<BaseApplicationMapper, BaseApplicationEntity> implements IBaseApplicationService {
    @Autowired
    BaseServiceMapper baseServiceMapper;
    @Autowired
    IBaseServiceService iBaseServiceService;
    @Override
    public void delApp(List<Long> ids) {
        HashMap<String, Object> selectMap = new HashMap<>();
        ids.forEach(appId->{
            selectMap.put("app_id",appId);
            String message = baseServiceMapper.selectByMap(selectMap).stream().map(BaseServiceEntity::getServiceName)
                    .collect(Collectors.joining(","));
            if(StrUtil.isNotBlank(message)){
                throw new BusinessException(String.format("请先删除服务:[%s]。",message));
            }
        });
        removeByIds(ids);
    }

    @Override
    public List<CascaderAppServiceDto> cascaderService() {
        Map<Long, List<BaseServiceEntity>> serviceListMap = iBaseServiceService.list().stream()
                .collect(Collectors.groupingBy(BaseServiceEntity::getAppId));
        return list().stream().map(app -> {
            CascaderAppServiceDto serviceDto = new CascaderAppServiceDto(app.getId(), app.getAppName(), AppServiceEnum.APP);
            List<BaseServiceEntity> baseServiceEntities = serviceListMap.get(app.getId());
            if(baseServiceEntities!=null){
                List<CascaderAppServiceDto> children=new ArrayList<>();
                baseServiceEntities.stream().forEach(serviceEntity -> {
                    children.add(new CascaderAppServiceDto(serviceEntity.getId(), serviceEntity.getServiceName(), AppServiceEnum.SERVICE));
                });
                serviceDto.setChildren(children);
            }
            return serviceDto;
        }).collect(Collectors.toList());
    }
}
