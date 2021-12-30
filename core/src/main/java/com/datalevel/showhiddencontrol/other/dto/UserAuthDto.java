package com.datalevel.showhiddencontrol.other.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.datalevel.showhiddencontrol.auth.AppServiceEnum;
import com.datalevel.showhiddencontrol.auth.dto.AuthFunDto;
import com.datalevel.showhiddencontrol.auth.entity.AuthFunEntity;
import com.datalevel.showhiddencontrol.auth.entity.AuthGroupEntity;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.EnumTypeHandler;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserAuthDto extends AuthFunEntity {
//    AuthGroupEntity
    @ApiModelProperty(value = "权限组名")
    @NotBlank(groups = {Update.class, Insert.class})
    private String authName;

    @ApiModelProperty(value = "code编码，数据级使用，目的降低字符长度")
    private Integer authCode;

    @ApiModelProperty(value = "组描述")
    private String authDesc;

    @ApiModelProperty(value = "app service 类型，枚举(app，service)")
    @TableField(typeHandler = EnumTypeHandler.class)
    @NotNull(groups = {Update.class, Insert.class})
    private AppServiceEnum appServiceType;

    @ApiModelProperty(value = "app service id 不同维度条件不同")
    @NotNull(groups = {Update.class, Insert.class})
    private Long appServiceId;
}
