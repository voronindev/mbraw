package ru.workspace.mbraw.webapp.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceTestConfiguration {

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("EmbeddedTestMbRawDb")
                .setScriptEncoding("UTF-8")
                .build();
    }

    @Bean
    public Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = -3632835913575726612L;

            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                setProperty("hibernate.connection.charSet", "UTF-8");
                setProperty("hibernate.hbm2ddl.auto", "create-drop");
            }
        };
    }

}
