package demo.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@Slf4j
public class AppDataSourceConfig {

    @Primary
    @Bean(name = "appDataSource")
    @ConfigurationProperties(prefix = "datasource.app")
    public DataSource appDataSource() {
        return new HikariDataSource();
    }


    @Primary
    @Bean(name = "appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("appDataSource") DataSource dataSource) {
        log.info("constructing appEntityManagerFactory");

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("demo.model.app")
                .persistenceUnit("app")
                .build();
    }


    @Primary
    @Bean(name = "appTransactionManager")
    public PlatformTransactionManager appTransactionManager(@Qualifier("appEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        log.info("constructing appTransactionManager");

        return new JpaTransactionManager(entityManagerFactory);
    }


    @Primary
    @Bean
    public JdbcTemplate appJdbcTemplate(@Qualifier("appDataSource") DataSource dataSource) {
        log.info("constructing appJdbcTemplate");

        return new JdbcTemplate(dataSource);
    }

}
