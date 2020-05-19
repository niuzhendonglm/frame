package com.niuzhendong.frame.component;

import net.hasor.core.ApiBinder;
import net.hasor.core.DimModule;
import net.hasor.db.JdbcModule;
import net.hasor.db.Level;
import net.hasor.spring.SpringModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author niuzhendong
 * @package_name com.niuzhendong.frame.component
 * @project_name frame
 * @date 2020/5/13
 */
@DimModule
@Component
public class ExampleModule implements SpringModule {

    @Autowired
    @Qualifier("slaveOneDataSource")
    private DataSource dataSource;

//    @Bean(name = "slaveOneDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.slave-one")
//    public DataSource slaveOneDateSource() {
//        return DataSourceBuilder.create().type(DruidDataSource.class).build();
//    }

    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new JdbcModule(Level.Full,this.dataSource));
    }
}
