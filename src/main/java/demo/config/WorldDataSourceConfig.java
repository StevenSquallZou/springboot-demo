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
    basePackages = "demo.repository.world",
    entityManagerFactoryRef = "worldEntityManagerFactory",
    transactionManagerRef = "worldTransactionManager"
)
@Slf4j
public class WorldDataSourceConfig {

    @Bean(name = "worldDataSource")
    @ConfigurationProperties(prefix = "datasource.world")
    public DataSource worldDataSource() {
        log.info("constructing worldDataSource");

        return new HikariDataSource();
    }


    @Bean(name = "worldEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean worldEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("worldDataSource") DataSource dataSource) {
        log.info("constructing worldEntityManagerFactory");

        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("demo.model.world")
                .persistenceUnit("world")
                .build();
    }


    @Bean(name = "worldTransactionManager")
    public PlatformTransactionManager worldTransactionManager(@Qualifier("worldEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        log.info("constructing worldTransactionManager");

        return new JpaTransactionManager(entityManagerFactory);
    }

}
