package demo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        log.info("main -> starting DemoApplication...");

        SpringApplication.run(DemoApplication.class, args);

        log.info("main -> started DemoApplication");
    }

}
