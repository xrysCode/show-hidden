package com.datalevel.showhiddencontrol.config;

import com.datalevel.showhiddencontrol.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    Environment env;
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e){
        //400参数不合法
        if(e instanceof MethodArgumentNotValidException){//注解校验不通过
            log.warn("注解校验不通过",e);
            FieldError fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
            if("prod".equals(env.getProperty("spring.profiles.active"))){
                return new ResponseResult(400,"");
            }else {
                return new ResponseResult(400,fieldError.getField()+fieldError.getDefaultMessage());
            }
        }
        if(e instanceof BindException){
            log.warn("注解校验不通过",e);
            FieldError fieldError = ((BindException) e).getBindingResult().getFieldError();
            if("prod".equals(env.getProperty("spring.profiles.active"))){
                return new ResponseResult(400,"");
            }else {
                return new ResponseResult(400,fieldError.getField()+fieldError.getDefaultMessage());
            }
        }
        if(e instanceof MissingServletRequestParameterException){
            log.warn("注解mvc缺少参数不通过",e);
            MissingServletRequestParameterException fieldError = (MissingServletRequestParameterException) e;
            if("prod".equals(env.getProperty("spring.profiles.active"))){
                return new ResponseResult(400,"");
            }else {
                return new ResponseResult(400,fieldError.getParameterName()+"缺少参数。");
            }
        }
        if(e instanceof HttpMessageNotReadableException){
            log.warn("json解析错误",e);
            if("prod".equals(env.getProperty("spring.profiles.active"))){
                return new ResponseResult(400,"");
            }else {
                return new ResponseResult(400,e.getMessage());
            }
        }
        if(e instanceof BusinessException){
            BusinessException businessException=(BusinessException)e;
            return new ResponseResult(businessException.getCode(),e.getMessage());
        }
        log.error("全局异常",e);
        return new ResponseResult(500,"出错了");
    }
}