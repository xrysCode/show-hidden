package com.datalevel.showhiddencontrol.sdk;

import com.datalevel.showhiddencontrol.communicate.TableSync;
import com.datalevel.showhiddencontrol.communicate.dto.TableScanDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Slf4j
public class TableAutoConfig {
    @Value("${auth.code}")
    String authCode;
    @Autowired
    TableSync tableSync;

//    @Bean
    public TableFieldScan localTableFieldScan(DataSource dataSource){
        TableFieldScan tableFieldScan = new TableFieldScan(dataSource);
        List<TableInfoDto> tableInfo = tableFieldScan.getLocalTableInfo();
        tableSync.syncTableInfo(new TableScanDto(authCode,tableInfo));
        log.info("本地表解析完毕");
        return tableFieldScan;
    }
}