package com.datalevel.showhiddencontrol.other.dto;

import com.datalevel.showhiddencontrol.other.entity.UserAuthEntity;
import com.datalevel.showhiddencontrol.other.entity.UserInfoEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserAuthDto extends UserInfoEntity {
    private Long userId;
    private Long authId;
    @ApiModelProperty(value = "权限组名")
    private String authName;
}