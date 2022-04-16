package com.javastart.notification.config;

import com.netflix.ribbon.proxy.annotation.ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
// mail-prop скрыт благодаря .gitignore, так как там были мои личные данные
// При pull проекта вам необходимо самостоятельно его настроить и расположить в resources сервиса notification
@PropertySource("classpath:mail-prop.properties")
public class MailConfig {

    @Autowired
    private Environment environment;

    @Bean
    JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smpt.gmail.com");
        javaMailSender.setPort(587);

        javaMailSender.setUsername(environment.getProperty("mail.username"));
        javaMailSender.setPassword(environment.getProperty("mail.password"));

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", environment.getProperty("mail.transport.protocol"));
        properties.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
        properties.put("mail.debug", environment.getProperty("mail.debug"));

        return  javaMailSender;
    }
}
