package com.qigetech.mark.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Spring boot方式
@EnableTransactionManagement
@Configuration
@MapperScan("com.qigetech.**.mapper*")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean //mybatis-plus带的分页插件
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}