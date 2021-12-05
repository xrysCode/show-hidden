package com.datalevel.showhiddencontrol.sdk;

import com.datalevel.showhiddencontrol.communicate.TableSync;
import com.datalevel.showhiddencontrol.communicate.dto.TableScanDto;
import com.datalevel.showhiddencontrol.sdk.auth.UserAuth;
import com.datalevel.showhiddencontrol.sdk.intercept.SqlAuthIntercept;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldScan;
import com.datalevel.showhiddencontrol.sdk.table.TableInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public TableFieldScan localTableFieldScan(DataSource dataSource){
        TableFieldScan tableFieldScan = new TableFieldScan(dataSource);
        List<TableInfoDto> tableInfo = tableFieldScan.getLocalTableInfo();
        SqlAuthIntercept.setNeedAuth(false);
        tableSync.syncTableInfo(new TableScanDto(authCode,tableInfo));
        SqlAuthIntercept.removeNeedAuth();
        log.info("本地表解析完毕");
        return tableFieldScan;
    }
    @Bean
    public UserAuth userAuth(TableFieldScan tableFieldScan){
        UserAuth userAuth = new UserAuth();
        userAuth.setAuthCode(authCode);
        userAuth.setTableFieldScan(tableFieldScan);
        return userAuth;
    }

}
