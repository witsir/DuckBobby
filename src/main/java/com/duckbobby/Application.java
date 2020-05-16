package com.duckbobby;

import com.duckbobby.common.SqlProp;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;


/**
 * 程序入口
 */
@MapperScan("com.duckbobby.dao")
@EnableAutoConfiguration
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.duckbobby")
@EnableScheduling
@ConfigurationProperties(prefix = "mysqlconfig")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Autowired
    SqlProp sqlProp;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/duckyou?autoReconnect=true&useSSL=false");
        dataSource.setUsername(sqlProp.getSqlUsername());
        dataSource.setPassword(sqlProp.getSqlUserPassword());
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
//@Bean
//public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
//    return new EmbeddedServletContainerCustomizer() {
//        @Override
//        public void customize(ConfigurableEmbeddedServletContainer container) {
//            container.setSessionTimeout(1, TimeUnit.MINUTES);
//            container.setPort(8080);
//        }
//    };
//}

}
