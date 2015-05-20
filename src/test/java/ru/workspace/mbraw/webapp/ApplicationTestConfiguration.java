package ru.workspace.mbraw.webapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.workspace.mbraw.webapp.conf.CommonConfiguration;
import ru.workspace.mbraw.webapp.conf.DataSourceTestConfiguration;

@SpringBootApplication
@Import(value = {CommonConfiguration.class, DataSourceTestConfiguration.class})
@Profile("test")
public class ApplicationTestConfiguration {

}
