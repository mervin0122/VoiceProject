package com.cn.mcc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 搭建基本环境
 * 1.导入数据库文件，创建department和employee表
 * 2.创建javaBean封装数据
 * 3.整合Mybatis操作数据库
 *  1）配置数据源信息
 *  2）结合mybatis
 *    ①mapper 接口类扫描包配置
 *   语音+ nlp+ nlu
 */
@MapperScan("com.cn.mcc.dao")
@SpringBootApplication
public class VoiceProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoiceProjectApplication.class, args);
		System.out.println("========================启动成功========================");
	}
}
