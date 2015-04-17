package ru.workspace.mbraw.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.workspace.mbraw.webapp.conf.CommonConfiguration;
import ru.workspace.mbraw.webapp.conf.DataSourceConfiguration;

@SpringBootApplication
@Import(value = {CommonConfiguration.class, DataSourceConfiguration.class})
public class ApplicationConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(ApplicationConfiguration.class);
        springApplication.run(args);
    }
}
