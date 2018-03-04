package com.core.base.mybatis.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: YNRepository 
 * @Description: 自定义注解, 映射文件中应用
 * @author 张龙龙 
 * @date 2018年3月3日 下午2:12:17 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface YNRepository {
    String value() default "";
}