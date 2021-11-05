package com.datalevel.showhiddencontrol.communicate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datalevel.showhiddencontrol.base.entity.BaseServiceEntity;
import com.datalevel.showhiddencontrol.base.entity.BaseTableFieldInfoEntity;
import com.datalevel.showhiddencontrol.base.mapper.BaseServiceMapper;
import com.datalevel.showhiddencontrol.base.service.IBaseTableFieldInfoService;
import com.datalevel.showhiddencontrol.communicate.dto.TableScanDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class TableSync {
    @Autowired
    IBaseTableFieldInfoService iBaseTableFieldInfoService;
    @Autowired
    BaseServiceMapper baseServiceMapper;

    /**
     * 同步表信息
     * @param tableScanDto
     */
//    @EventListener
    @Transactional
    public void syncTableInfo(TableScanDto tableScanDto){
        LambdaQueryWrapper<BaseServiceEntity> queryWrapper = new QueryWrapper<BaseServiceEntity>()
                .lambda().eq(BaseServiceEntity::getServiceCode, tableScanDto.getAuthCode());
        BaseServiceEntity serviceEntity = baseServiceMapper.selectOne(queryWrapper);
        if(serviceEntity==null){
            log.error("不存在的服务code");
            return;
        }
        LambdaQueryWrapper<BaseTableFieldInfoEntity> queryWrapper1 = new QueryWrapper<BaseTableFieldInfoEntity>()
                .lambda().eq(BaseTableFieldInfoEntity::getServiceId, serviceEntity.getId());

        Map<Boolean, Map<String, BaseTableFieldInfoEntity>> allMap = iBaseTableFieldInfoService.getBaseMapper().selectList(queryWrapper1).stream()
                .collect(Collectors.groupingBy(tableFieldInfoEntity -> tableFieldInfoEntity.getParentId() == null,
                        Collectors.toMap(BaseTableFieldInfoEntity::getTableFieldName, Function.identity())));
        Map<String, BaseTableFieldInfoEntity> tableMap =allMap.getOrDefault(true, Maps.newHashMap());
        Map<String, BaseTableFieldInfoEntity> fieldMap = allMap.getOrDefault(false,Maps.newHashMap());
        fieldMap.forEach((k,v)->{});///这里有问题


        LinkedList<BaseTableFieldInfoEntity> updateTableInfoList = Lists.newLinkedList();
        @NotNull(groups = Update.class) Long serviceId = serviceEntity.getId();
        tableScanDto.getTableInfoList().stream().forEach(tableInfoDto -> {
            BaseTableFieldInfoEntity tableInfoEntity = tableMap.remove(tableInfoDto.getName());
            if(tableInfoEntity==null){
                tableInfoEntity = new BaseTableFieldInfoEntity()
                        .setParentId(null).setServiceId(serviceId).setIsDeprecated(false)
                        .setTableFieldName(tableInfoDto.getName())
                        .setComment(tableInfoDto.getComment());
                iBaseTableFieldInfoService.save(tableInfoEntity);
            }else {
                tableInfoEntity.setComment(tableInfoDto.getComment()).setIsDeprecated(false).setCreateUser(null).setCreateTime(null);
                updateTableInfoList.add(tableInfoEntity);
            }

            Long tableId = tableInfoEntity.getId();
            tableInfoDto.getFieldList().forEach(tableFieldDto -> {
                BaseTableFieldInfoEntity fieldInfoEntity = fieldMap.remove(tableFieldDto.getField());
                if(fieldInfoEntity==null){
                    fieldInfoEntity = new BaseTableFieldInfoEntity()
                            .setParentId(tableId).setServiceId(serviceId).setIsDeprecated(false)
                            .setTableFieldName(tableInfoDto.getName())
                            .setComment(tableInfoDto.getComment());
                    iBaseTableFieldInfoService.save(fieldInfoEntity);
                }else {
                    fieldInfoEntity.setComment(tableInfoDto.getComment()).setIsDeprecated(false).setCreateUser(null).setCreateTime(null);
                    updateTableInfoList.add(fieldInfoEntity);
                }
            });
        });
        tableMap.forEach((k,v)->{
            v.setIsDeprecated(true).setCreateUser(null).setCreateTime(null);
            updateTableInfoList.add(v);
        });
        fieldMap.forEach((k,v)->{
            v.setIsDeprecated(true).setCreateUser(null).setCreateTime(null);
            updateTableInfoList.add(v);
        });
        iBaseTableFieldInfoService.updateBatchById(updateTableInfoList);
    }


}
