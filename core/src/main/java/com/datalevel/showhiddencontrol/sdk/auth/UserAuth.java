package com.datalevel.showhiddencontrol.sdk.auth;

import com.datalevel.showhiddencontrol.other.dto.UserAuthInfo;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldDto;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldScan;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import com.datalevel.showhiddencontrol.sdk.table.TableInfoDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserAuth {
    static String authCode;
    static TableFieldScan tableFieldScan;
    public static String appKeysField="auth_app_keys";
    public static String serviceKeysField="auth_service_keys";

    /**
     * k 表名
     */
    static Map<String,TableInfoDto> appKeysTableMap=new ConcurrentHashMap<>();
    static Map<String,TableInfoDto> serviceKeysTableMap=new ConcurrentHashMap<>();

    static IUserAuthService iUserAuthService;
//    @Value("${auth.code}")
    public void setAuthCode(String authCode) {
        this.authCode=authCode;
    }

//    @Autowired
    public void setTableFieldScan(TableFieldScan tableFieldScan) {
        UserAuth.tableFieldScan = tableFieldScan;
        tableFieldScan.getLocalTableInfo().stream().forEach(tableInfoDto -> {
            for (TableFieldDto tableFieldDto : tableInfoDto.getFieldList()) {
                if (tableFieldDto.getField().equals(appKeysField)) {
                    appKeysTableMap.put(tableInfoDto.getName(),tableInfoDto);
                }
                if (tableFieldDto.getField().equals(serviceKeysField)) {
                    serviceKeysTableMap.put(tableInfoDto.getName(),tableInfoDto);
                }
            }
        });
    }
    public static Long getUserId(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        String userId = request.getHeader("Token");
        return Long.parseLong(userId);
    }

    /**
     *
     * @return k 字段 v 字段的值
     */
    public static Map<String, String> getUserAuthInfo(String tableName){
        Long userId = getUserId();
        UserAuthInfo userAuthInfo = iUserAuthService.getUserBackendAuthKeys(authCode, userId);

        UserAuthInfo.DataAuth dataAuth = userAuthInfo.getDataAuth();
        HashMap<String, String> fieldValueMap = new HashMap<>();
        if(appKeysTableMap.containsKey(tableName)){
            fieldValueMap.put(appKeysField,getRegexValue(dataAuth.getAppKeys()));
        }
        if(serviceKeysTableMap.containsKey(tableName)){
            fieldValueMap.put(serviceKeysField,getRegexValue(dataAuth.getServiceKeys()));
        }
        return fieldValueMap;
    }

    private static String getRegexValue(List<Integer> keys) {
        if(keys.size()==0){
            return null;
        }
        StringBuilder authKeys = new StringBuilder();
        for (Integer authKey: keys) {
            authKeys.append("\\\\b").append(authKey).append("\\\\b").append("|");
        }
        authKeys.deleteCharAt(authKeys.lastIndexOf("|")-1);
        return authKeys.toString();
    }

    public static void main(String[] args) {
        String regexValue = getRegexValue(Lists.newLinkedList());
    }
}
