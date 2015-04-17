package ru.workspace.mbraw.webapp.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class CommonConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Properties hibernateProperties;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("ru.workspace.mbraw.webapp.pojo");
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }
}
