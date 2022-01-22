package com.datalevel.showhiddencontrol.other.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

@Getter
@Accessors(chain = true)
public class UserAuthInfo {
    @Setter
    private Long userId;

    @ApiModelProperty(value = "接口权限")
    private Set<ApiAuth> apiAuthSet=new HashSet<>();
    @ApiModelProperty(value = "数据权限")//所有满足auth关键字的数据都能查看。
    private DataAuth dataAuth=new DataAuth();

    public UserAuthInfo addApiAuth(String requestUrl, List<String> requestMethods) {
        apiAuthSet.add(new ApiAuth(requestUrl,requestMethods));
        return this;
    }

    @Getter
    @Accessors(chain = true)
    class ApiAuth{
        public ApiAuth(String requestUrl, List<String> requestMethods) {
            this.requestUrl = requestUrl;
            this.requestMethods = requestMethods;
        }

        @ApiModelProperty(value = "后端访问接口")
        private String requestUrl;

        @ApiModelProperty(value = "请求方法")
        private List<String> requestMethods;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ApiAuth apiAuth = (ApiAuth) o;
            return requestUrl.equals(apiAuth.requestUrl) &&
                    requestMethods.toString().equals(apiAuth.requestMethods.toString());
        }

        @Override
        public int hashCode() {
            return Objects.hash(requestUrl, requestMethods.toString());
        }
    }
    @Getter
    @Accessors(chain = true)
    public class DataAuth{
        @ApiModelProperty(value = "应用级的权限keys")
        private List<Integer> appKeys=new LinkedList<>();

        @ApiModelProperty(value = "应用拆分级的权限keys")
        private List<Integer> serviceKeys=new LinkedList<>();
    }
}
