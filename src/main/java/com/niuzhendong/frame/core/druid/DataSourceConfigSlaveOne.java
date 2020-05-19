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
@MapperScan(basePackages = {"com.niuzhendong.frame.modules.test.mapper"}, sqlSessionTemplateRef = "slaveOneSqlSessionTemplate")
@EnableJpaRepositories(basePackages = {"com.niuzhendong.frame.modules.test.repository"},entityManagerFactoryRef = "slaveOneEntityManagerFactory",transactionManagerRef = "slaveOneJPATransactionManager")
@EnableTransactionManagement
public class DataSourceConfigSlaveOne {

    @Bean(name = "slaveOneDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave-one")
    public DataSource slaveOneDateSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "slaveOneSqlSessionFactory")
    public SqlSessionFactory slaveOneSqlSessionFactory(@Qualifier("slaveOneDataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mappers/test/*.xml"));
        return bean.getObject();
    }

    /**
     * 配置事务管理
     */
    @Bean(name = "slaveOneTransactionManager")
    public DataSourceTransactionManager slaveOneTransactionManager(@Qualifier("slaveOneDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "slaveOneSqlSessionTemplate")
    public SqlSessionTemplate slaveOneSqlSessionTemplate(@Qualifier("slaveOneSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    /**
     * JPA
     */
    @Autowired
    private Properties slaveOneJpaProperties;

    @Bean(name = "slaveOneEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean slaveOneEntityManagerFactory(EntityManagerFactoryBuilder builder) throws Exception {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                =  builder
                .dataSource(slaveOneDateSource())
                .packages("com.niuzhendong.frame.modules.test.entity")//设置实体类所在位置
                .persistenceUnit("slaveOnePersistenceUnit")//持久化单元创建一个默认即可，多个便要分别命名
                .build();
        localContainerEntityManagerFactoryBean.setJpaProperties(slaveOneJpaProperties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "slaveOneEntityManager")
    public EntityManager slaveOneEntityManager(EntityManagerFactoryBuilder builder) throws Exception {
        return slaveOneEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "slaveOneJPATransactionManager")
    public PlatformTransactionManager slaveOneJPATransactionManager(EntityManagerFactoryBuilder builder) throws Exception {
        return new JpaTransactionManager(slaveOneEntityManagerFactory(builder).getObject());
    }
}
