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

    @Value("${APP_HOSTNAME:localhost}")
    protected String appHostname;

    @Value("${APP_PORT:${server.port}}")
    protected int appPort;


    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info();
        info.setTitle("SpringBoot-Demo API");
        info.setVersion(appVersion);
        info.setDescription("SpringBoot-Demo API specifications");

        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info);
        String url = "http://" + appHostname + ":" + appPort;
        log.info("set server url to {}", url);
        openAPI.addServersItem(new Server().url(url));

        return openAPI;
    }

}
