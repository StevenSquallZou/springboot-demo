package demo.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class SwaggerConfig {
    @Value("${APP_VERSION}")
    protected String appVersion;


    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info();
        info.setTitle("SpringBoot-Demo API");
        info.setVersion(appVersion);
        info.setDescription("SpringBoot-Demo API specifications");

        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        openAPI.addServersItem(new Server().url("http://springboot-demo.com:30080"));
        log.info("Overriding server url to http://springboot-demo.com:30080");

        return openAPI;
    }

}
