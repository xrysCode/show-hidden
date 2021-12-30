package com.datalevel.showhiddencontrol.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    @Autowired
    Environment environment;

    /**
     * Every Docket bean is picked up by the swagger-mvc framework - allowing for multiple
     * swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean(name = "base")
    public Docket createRestApiBase(){
        return createRestApi("基础数据","com.datalevel.showhiddencontrol.base.controller");
    }

    @Bean(name = "auth")
    public Docket createRestApiAuth(){
        return createRestApi("权限配置","com.datalevel.showhiddencontrol.auth.controller");
    }
    @Bean(name = "other")
    public Docket createRestApiOther(){
        return createRestApi("其他非核心模块","com.datalevel.showhiddencontrol.other.controller");
    }
    private Docket createRestApi(String groupName ,String basePackage){
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        Parameter build = ticketPar.name("token")
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(build);
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)//..globalResponseMessage()
                .apiInfo(apiInfo()).groupName(groupName);
        if("prod".equals(environment.getProperty("spring.profiles.active"))){
            docket.enable(false);
        }
        //全局状态码
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        ResponseMessage response400 = new ResponseMessageBuilder().code(400)
                .message("传参有误")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response400);
        ResponseMessage response401 = new ResponseMessageBuilder().code(401)
                .message("未认证请登录")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response401);
        ResponseMessage response403 = new ResponseMessageBuilder().code(403)
                .message("无权限拒绝执行")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response403);
        ResponseMessage response1000 = new ResponseMessageBuilder().code(1000)
                .message("1000以上，业务数据验证未通过")//主要用于弹框
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response1000);
        docket.globalResponseMessage(RequestMethod.GET,responseMessages);
        docket.globalResponseMessage(RequestMethod.POST,responseMessages);
        docket.globalResponseMessage(RequestMethod.PUT,responseMessages);
        docket.globalResponseMessage(RequestMethod.DELETE,responseMessages);

        docket.useDefaultResponseMessages(false);
        return docket;
    }


//    @Bean(name = "admin")
    public Docket createRestApi2(){
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        Parameter build = ticketPar.name("Token")
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(build);
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.datalevel.showhiddencontrol.core.controller,com.travelCat.controller.admin"))
//                .apis(SwaggerConfig.basePackage("com.travelCat.controller.admin,com.travelCat.controller.homeLogin"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)//..globalResponseMessage()
                .apiInfo(apiInfo()).groupName("admin");
        if("prod".equals(environment.getProperty("spring.profiles.active"))){
            docket.enable(false);
        }
        //全局状态码
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        ResponseMessage response400 = new ResponseMessageBuilder().code(400)
                .message("传参有误")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response400);
        ResponseMessage response401 = new ResponseMessageBuilder().code(401)
                .message("未认证请登录")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response401);
        ResponseMessage response403 = new ResponseMessageBuilder().code(403)
                .message("无权限拒绝执行")//说明
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response403);
        ResponseMessage response1000 = new ResponseMessageBuilder().code(1000)
                .message("1000以上，业务数据验证未通过")//主要用于弹框
                .responseModel(new ModelRef("ResponseResult«Void»"))//schema
                .build();
        responseMessages.add(response1000);
        docket.globalResponseMessage(RequestMethod.GET,responseMessages);
        docket.globalResponseMessage(RequestMethod.POST,responseMessages);
        docket.globalResponseMessage(RequestMethod.PUT,responseMessages);
        docket.globalResponseMessage(RequestMethod.DELETE,responseMessages);

        docket.useDefaultResponseMessages(false);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口文档-"+environment.getProperty("spring.profiles.active"))
                .description("cat服务端"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .version("1.0.0")
                .build();
    }

    //多包扫描
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(",")) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}