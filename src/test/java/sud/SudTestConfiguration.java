package sud;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import sud.config.DataSourceConfig;

@Configuration
@ComponentScan(basePackages = {"sud.**", })
@EnableJpaRepositories("sud.**")
@ContextConfiguration(classes = {
        TestConfiguration.class,
        DataSourceConfig.class})

public class SudTestConfiguration {
}
