package com.datalevel.showhiddencontrol.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限组与功能关系，多对多
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("auth_fun")
@ApiModel(value="AuthFunEntity对象", description="权限组与功能关系，多对多")
public class AuthFunEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull(groups = {Update.class, Insert.class})
    private Long funId;
    @NotNull(groups = {Update.class, Insert.class})
    private Long authId;

    private LocalDateTime createTime;

    private String createUser;


}
