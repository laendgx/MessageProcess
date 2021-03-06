package com.boco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
//@ComponentScan("com.boco.domain")
@MapperScan(basePackages = {"com.boco.mapper"})//在mapper中不用添加注解，在此处指明要扫描的mapper所在的包路径。通过反射技术读取该包下面的所有类
public class CollCtrlSvrMain {
    /**
     * springboot的main函数
     */
    public static void main(String[] args) {
        SpringApplication.run(CollCtrlSvrMain.class, args);
    }

    /**
     * 消息转换器使用了RabbitMQ自带的Jackson2JsonMessageConverter转换器，用于消息队列JSON类型的传输转换
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
