package com.datalevel.showhiddencontrol.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * 微服务粒度
 * </p>
 *
 * @author xry
 * @since 2021-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("base_service")
@ApiModel(value="BaseServiceEntity对象", description="微服务粒度")
public class BaseServiceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups ={Insert.class,Update.class} )
    private Long appId;

    @ApiModelProperty(value = "app服务标记,新增自动生成")
    private String serviceCode;

    @ApiModelProperty(value = "子服务名")
    @NotBlank(groups ={Insert.class,Update.class} )
    private String serviceName;

    @ApiModelProperty(value = "模块服务名")
    private String serviceDesc;

    @ApiModelProperty(value = "回调地址")
    @NotBlank(groups ={Insert.class,Update.class} )
    private String callBackUrl;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;


}
