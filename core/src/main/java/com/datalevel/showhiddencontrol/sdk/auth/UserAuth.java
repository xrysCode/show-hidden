package com.datalevel.showhiddencontrol.sdk.auth;

import com.datalevel.showhiddencontrol.sdk.table.TableFieldDto;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldScan;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import com.datalevel.showhiddencontrol.sdk.table.TableInfoDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserAuth {

    static TableFieldScan tableFieldScan;
    @Autowired
    public void setTableFieldScan(TableFieldScan tableFieldScan) {
        UserAuth.tableFieldScan = tableFieldScan;
        Map<String, TableInfoDto> authMap = tableFieldScan.getLocalTableInfo().stream().filter(tableInfoDto -> {
            boolean needAuth = false;
            for (TableFieldDto tableFieldDto : tableInfoDto.getFieldList()) {
                if (tableFieldDto.getField().equals("auth_keys")) {
                    needAuth = true;
                    break;
                }
            }
            return needAuth;
        }).collect(Collectors.toMap(TableInfoDto::getName, Function.identity()));
    }

    public static Map<String, TableFieldValue> getUserAuthInfo(){

        List<String> userAuths= Lists.newArrayList("a","b");
        StringBuilder authKeys = new StringBuilder();
        for (String authKey: userAuths) {
            authKeys.append("\\\\b").append(authKey).append("\\\\b").append("|");
        }
        authKeys.deleteCharAt(authKeys.lastIndexOf("|")-1);
        Map<String, TableFieldValue> tableAuthMap=new HashMap<>();
        tableAuthMap.put("other_menus",new TableFieldValue("other_menus","auth_keys",authKeys.toString()));
        return tableAuthMap;
    }
}
