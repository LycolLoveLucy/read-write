package com.application.config;

import com.application.dynamicdatasource.routing.DbType;
import com.application.dynamicdatasource.routing.RoutingDataSource;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import plus.CustomerSqlInjector;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.application.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfig {



    @Bean(name = "readSource")
    @Primary
    public DataSource readSource() {
        return DataSourceBuilder.create().url("jdbc:h2:mem:test;MODE=Oracle").username("root")
                .password("123456")
                .driverClassName("org.h2.Driver").build();
    }

    @Bean(name = "writeSource")
    public DataSource writeSource() {
       return DataSourceBuilder.create().url("jdbc:h2:mem:write_db;MODE=Oracle").username("root")
                .password("123456")
                .driverClassName("org.h2.Driver").build();
    }


    @Bean("routingDataSource")
    @Qualifier("routingDataSource")
    public RoutingDataSource routingDataSource(){
        RoutingDataSource routingDataSource=  new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbType.READ,readSource());
        targetDataSources.put(DbType.WRITE,writeSource());
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get(DbType.READ));
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();
        return  routingDataSource;
    }


 /*   @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory dbSqlSessionFactory(@Qualifier("routingDataSource") RoutingDataSource dataSource
                                                 ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
*/

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager dbTransactionManager(@Qualifier("routingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate dbSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean(name = "globalConfiguration")
    @Primary
    public GlobalConfig globalConfiguration() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlInjector(new CustomerSqlInjector());
        return globalConfig;
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory dbSqlSessionFactory(@Qualifier("routingDataSource") DataSource dataSource,
                                                 @Qualifier("globalConfiguration") GlobalConfig globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setGlobalConfig(globalConfiguration);
        return bean.getObject();
    }





}
