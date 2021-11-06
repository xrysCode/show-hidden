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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

        Map<Boolean, List<BaseTableFieldInfoEntity>> allMap = iBaseTableFieldInfoService.getBaseMapper().selectList(queryWrapper1).stream()
                .collect(Collectors.groupingBy(tableFieldInfoEntity -> tableFieldInfoEntity.getParentId() == null));
        Map<Long, BaseTableFieldInfoEntity> tableIdMap = new HashMap<>();
        Map<String, BaseTableFieldInfoEntity> tableNameMap = new HashMap<>();
        allMap.getOrDefault(true, Lists.newLinkedList()).forEach((v)->{
            tableIdMap.put(v.getId(),v);
            tableNameMap.put(v.getDbTableField(),v);
        });
        Map<String, BaseTableFieldInfoEntity> fieldNameMap = new HashMap<>();
        allMap.getOrDefault(false,Lists.newLinkedList()).forEach((v)->{
            fieldNameMap.put(String.format("%s:%s",tableIdMap.get(v.getParentId()).getDbTableField(),v.getDbTableField()),v);
        });

        LinkedList<BaseTableFieldInfoEntity> updateTableInfoList = Lists.newLinkedList();
        @NotNull(groups = Update.class) Long serviceId = serviceEntity.getId();
        tableScanDto.getTableInfoList().stream().forEach(tableInfoDto -> {
            BaseTableFieldInfoEntity tableInfoEntity = tableNameMap.remove(tableInfoDto.getName());
            if(tableInfoEntity==null){
                tableInfoEntity = new BaseTableFieldInfoEntity()
                        .setParentId(null).setServiceId(serviceId).setIsDeprecated(false)
                        .setDbTableField(tableInfoDto.getName())
                        .setDbComment(tableInfoDto.getComment());
                iBaseTableFieldInfoService.save(tableInfoEntity);//提前保持以便获取id
            }else {
                tableInfoEntity.setDbComment(tableInfoDto.getComment()).setIsDeprecated(false).setCreateUser(null).setCreateTime(null);
                updateTableInfoList.add(tableInfoEntity);
            }

            Long tableId = tableInfoEntity.getId();
            String tableName = tableInfoEntity.getDbTableField();
            tableInfoDto.getFieldList().forEach(tableFieldDto -> {
                String key=String.format("%s:%s",tableName,tableFieldDto.getField());
                BaseTableFieldInfoEntity fieldInfoEntity = fieldNameMap.remove(key);
                if(fieldInfoEntity==null){
                    fieldInfoEntity = new BaseTableFieldInfoEntity()
                            .setParentId(tableId).setServiceId(serviceId).setIsDeprecated(false)
                            .setDbTableField(tableFieldDto.getField())
                            .setDbComment(tableFieldDto.getComment());
                    iBaseTableFieldInfoService.save(fieldInfoEntity);
                }else {
                    fieldInfoEntity.setDbComment(tableFieldDto.getComment()).setIsDeprecated(false).setCreateUser(null).setCreateTime(null);
                    updateTableInfoList.add(fieldInfoEntity);
                }
            });
        });
        tableNameMap.forEach((k,v)->{
            v.setIsDeprecated(true).setCreateUser(null).setCreateTime(null);
            updateTableInfoList.add(v);
        });
        fieldNameMap.forEach((k,v)->{
            v.setIsDeprecated(true).setCreateUser(null).setCreateTime(null);
            updateTableInfoList.add(v);
        });
        iBaseTableFieldInfoService.updateBatchById(updateTableInfoList);
    }


}
