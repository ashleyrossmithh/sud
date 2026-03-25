package sud.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class DataSourceConfig {

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();

        // 1. Драйвер оставляем
        dataSource.setDriverClassName("org.postgresql.Driver");

        // 2. Меняем URL на твой удаленный сервер
        dataSource.setUrl("jdbc:postgresql://109.198.190.115:32322/MakarovM");

        // 3. Меняем пользователя и пароль
        dataSource.setUsername("student");
        dataSource.setPassword("5432");

        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(100);

        return dataSource;
    }


}
