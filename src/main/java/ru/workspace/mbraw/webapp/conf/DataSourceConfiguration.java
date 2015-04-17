package ru.workspace.mbraw.webapp.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfiguration {

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/mbdb");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        return ds;
    }

    @Bean
    public Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = -3632835913575726612L;

            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
                setProperty("hibernate.connection.charSet", "UTF-8");
                setProperty("hibernate.hbm2ddl.auto", "update");
            }
        };
    }
}
