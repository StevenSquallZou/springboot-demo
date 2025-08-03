package demo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Value("${APP_VERSION}")
    protected String appVersion;


    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info();
        info.setTitle("SpringBoot-Demo API");
        info.setVersion(appVersion);
        info.setDescription("SpringBoot-Demo API specifications");

        return new OpenAPI().info(info);
    }

}
