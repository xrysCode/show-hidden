package com.datalevel.showhiddencontrol.sdk.auth;

import com.datalevel.showhiddencontrol.other.dto.UserAuthInfo;
import com.datalevel.showhiddencontrol.other.service.IUserAuthService;
import com.datalevel.showhiddencontrol.sdk.intercept.SqlAuthIntercept;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldDto;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldScan;
import com.datalevel.showhiddencontrol.sdk.table.TableFieldValue;
import com.datalevel.showhiddencontrol.sdk.table.TableInfoDto;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

//这个类用来临时代替远程交换用户信息
@Slf4j
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

    //这个用来代替远程查询用户权限
    static IUserAuthService iUserAuthService;

    public UserAuth(String authCode,TableFieldScan tableFieldScan,IUserAuthService iUserAuthService) {
        UserAuth.authCode=authCode;
        UserAuth.iUserAuthService=iUserAuthService;
        setTableFieldScan( tableFieldScan);
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
        String userId = request.getHeader("token");
        if(userId==null){
            log.error("没有用户");
            return null;
        }
        return Long.parseLong(userId);
    }

    /**
     *
     * @return k 字段 v 字段的值
     */
    public static Map<String, String> getUserAuthInfo(String tableName){
        Long userId = getUserId();
        SqlAuthIntercept.setNeedAuth(false);
        UserAuthInfo userAuthInfo = iUserAuthService.getUserBackendAuthKeys(authCode, userId);
        SqlAuthIntercept.setNeedAuth(true);

        UserAuthInfo.DataAuth dataAuth = userAuthInfo.getDataAuth();
        HashMap<String, String> fieldValueMap = new HashMap<>();
        if(appKeysTableMap.containsKey(tableName)&&dataAuth.getAppKeys().size()>0){
            fieldValueMap.put(appKeysField,getRegexValue(dataAuth.getAppKeys()));
        }
        if(serviceKeysTableMap.containsKey(tableName)&&dataAuth.getServiceKeys().size()>0){
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
        authKeys.deleteCharAt(authKeys.lastIndexOf("|"));
        return authKeys.toString();
    }

    public static void main(String[] args) {
        String regexValue = getRegexValue(Lists.newLinkedList());
    }
}
