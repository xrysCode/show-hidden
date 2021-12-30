package com.datalevel.showhiddencontrol.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;

/**
 * @Author xry
 * @Date 2021/12/28 18:52
 */
@Configuration
public class JsonConfig {
//    @Bean
//    public StdConverter<Long,String> createConverter(){
//        return new StdConverter<Long,String>(){
//            @Override
//            public String convert(Long value) {
//                return value.toString();
//            }
//        };
//    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(Long.class,new ToStringSerializer());
    }
}
