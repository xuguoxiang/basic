package com.xiang.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.cache.CacheBuilder;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.Controller;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/25.
 */
@Configuration
@PropertySource(value = {"classpath:jdbc.properties"})
@EnableCaching
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.xiang"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION, value = {Controller.class, RestController.class, ControllerAdvice.class})})
public class AppConfig {
    @Autowired
    private Environment env;

    /**
     * 配置数据源
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driver"));
        dataSource.setUrl(env.getProperty("jdbc.driver"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setPassword(env.getProperty("jdbc.maxActive"));
        dataSource.setPassword(env.getProperty("jdbc.maxWait"));

        dataSource.setRemoveAbandoned(env.getProperty("jdbc.removeAbandoned", Boolean.class));
        dataSource.setRemoveAbandonedTimeout(env.getProperty("jdbc.removeAbandonedTimeout", Integer.class));
        dataSource.setValidationQuery(env.getProperty("jdbc.validationQuery"));
        dataSource.setTestOnBorrow(env.getProperty("jdbc.testOnBorrow", Boolean.class));

        return dataSource;
    }

    /**
     * 创建namedJdbcTemplate
     *
     * @return
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    /**
     * 声明事务
     *
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * 引入google缓存
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        List list = new ArrayList();
        list.add(new GuavaCache("APP_CACHE", CacheBuilder.newBuilder().<Object, Object>build()));
        manager.setCaches(list);

        return manager;
    }

    @Bean
    MongoDbFactory mongoDbFactory(){
        MongoClient mongo = new MongoClient(env.getProperty("mongo.host"), env.getProperty("mongo.port", Integer.class));
        SimpleMongoDbFactory mongoDbFactory= new SimpleMongoDbFactory(mongo, env.getProperty("mongo.dbName"));
        mongoDbFactory.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return mongoDbFactory;
    }

    @Bean
    MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoDbFactory());
    }
}
