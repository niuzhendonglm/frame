package com.niuzhendong.frame.core.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * 配置多数据源
 * @author niuzhendong
 */
@Configuration
@MapperScan(basePackages = {"com.niuzhendong.frame.modules.sys.mapper"},sqlSessionFactoryRef = "masterSqlSessionFactory", sqlSessionTemplateRef = "masterSqlSessionTemplate")
@EnableJpaRepositories(basePackages = {"com.niuzhendong.frame.modules.sys.repository"},entityManagerFactoryRef = "masterEntityManagerFactory",transactionManagerRef = "masterJPATransactionManager")
@EnableTransactionManagement
public class DataSourceConfigMaster {

    @Bean(name = "masterDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource masterDateSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mappers/sys/*.xml"));
        return bean.getObject();
    }

    /**
     * 配置事务管理
     */
    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    /**
     * JPA
     */
    @Autowired
    private Properties masterJpaProperties;

    @Primary
    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(EntityManagerFactoryBuilder builder) throws Exception {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                =  builder
                .dataSource(masterDateSource())
                .packages("com.niuzhendong.frame.modules.sys.entity")//设置实体类所在位置
                .persistenceUnit("masterPersistenceUnit")//持久化单元创建一个默认即可，多个便要分别命名
                .build();
        localContainerEntityManagerFactoryBean.setJpaProperties(masterJpaProperties);
        return localContainerEntityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "masterEntityManager")
    public EntityManager masterEntityManager(EntityManagerFactoryBuilder builder) throws Exception {
        return masterEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "masterJPATransactionManager")
    public PlatformTransactionManager masterJPATransactionManager(EntityManagerFactoryBuilder builder) throws Exception {
        return new JpaTransactionManager(masterEntityManagerFactory(builder).getObject());
    }
}
