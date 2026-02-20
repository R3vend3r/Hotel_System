package hotel_system.Utils;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase(Environment env) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(env.getProperty("liquibase.change-log"));
        liquibase.setShouldRun(Boolean.parseBoolean(env.getProperty("liquibase.enabled")));
        return liquibase;
    }
}