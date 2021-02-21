package com.springboot.frame.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * 数据备份配置类: @PreDestroy
 *
 * @author yangguanghui6
 * @date 2021/2/21 17:19
 */
@Configuration
public class DataBackupConfig {

    @PreDestroy
    public void backData() {
        System.out.println("数据正在备份...！");
    }

}
