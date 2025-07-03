package demo.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "demo.repository.sakila",
    entityManagerFactoryRef = "sakilaEntityManagerFactory",
    transactionManagerRef = "sakilaTransactionManager"
)
@Slf4j
public class SakilaDataSourceConfig {

    @Bean(name = "sakilaDataSource")
    @ConfigurationProperties(prefix = "datasource.sakila")
    public DataSource sakilaDataSource() {
        log.info("constructing sakilaDataSource");

        return new HikariDataSource();
    }


    @Bean(name = "sakilaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sakilaEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("sakilaDataSource") DataSource dataSource) {
        log.info("constructing sakilaEntityManagerFactory");

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("demo.model.sakila")
                .persistenceUnit("sakila")
                .build();
    }


    @Bean(name = "sakilaTransactionManager")
    public PlatformTransactionManager sakilaTransactionManager(@Qualifier("sakilaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        log.info("constructing sakilaTransactionManager");

        return new JpaTransactionManager(entityManagerFactory);
    }

}
