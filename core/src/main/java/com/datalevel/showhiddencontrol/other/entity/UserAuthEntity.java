package com.datalevel.showhiddencontrol.other.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author xry
 * @since 2021-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_auth")
@ApiModel(value="UserAuthEntity对象", description="")
public class UserAuthEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull(groups = {Insert.class, Update.class})
    private Long userId;
    @NotNull(groups = {Insert.class, Update.class})
    private Long authId;


}
