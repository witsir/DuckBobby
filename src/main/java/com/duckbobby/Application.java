package com.duckbobby;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;


/**
 * 程序入口
 */
@MapperScan("com.duckbobby.dao")
@EnableAutoConfiguration
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.duckbobby")
@EnableScheduling
public class Application{
    public static void main(String[] args) throws Exception {
//        new SpringApplicationBuilder().bannerMode(Banner.Mode.OFF);
        SpringApplication application = new SpringApplication(Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }


    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/duckyou?autoReconnect=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("910131");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("12800KB");
        factory.setMaxRequestSize("12800KB");
        return factory.createMultipartConfig();
    }


//原配置implements EmbeddedServletContainerCustomizer
//    @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        container.setPort(8080);
//    }
//通过EmbeddedServletContainerCustomizer接口调优Tomcat
@Bean
public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
    return new EmbeddedServletContainerCustomizer() {
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.setSessionTimeout(1, TimeUnit.MINUTES);
            container.setPort(8080);
        }
    };
}

}
