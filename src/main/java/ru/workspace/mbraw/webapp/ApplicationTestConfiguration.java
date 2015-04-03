package ru.workspace.mbraw.webapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.workspace.mbraw.webapp.conf.CommonConfiguration;
import ru.workspace.mbraw.webapp.conf.DataSourceTestConfiguration;

@SpringBootApplication
@Import(value = {CommonConfiguration.class, DataSourceTestConfiguration.class})
public class ApplicationTestConfiguration {

}
