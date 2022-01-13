package com.github.flashvayne.autoconfig;

import com.github.flashvayne.interceptor.MysqlPageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MysqlPage自动装配
 *
 * @author flashvayne
 */
@Configuration
public class MysqlPageAutoConfig implements InitializingBean {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public MysqlPageAutoConfig(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void afterPropertiesSet() {
        MysqlPageInterceptor mysqlPageInterceptor = new MysqlPageInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!configuration.getInterceptors().contains(mysqlPageInterceptor)) {
                configuration.addInterceptor(mysqlPageInterceptor);
            }
        }
    }

}
