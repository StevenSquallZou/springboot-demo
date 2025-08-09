package demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RefreshScope
@Slf4j
public class DemoController {
    @Value("${springboot-demo.switchFlag}")
    protected String switchFlag;


    /**
     * Sample: <a href="http://localhost:8080/hello?content=Steven">...</a>
     * @param content - the content to include in the greeting, defaults to "World"
     * @return Hello {content}!
     */
    @GetMapping("/hello")
    public String sayHello(@RequestParam(name = "content", defaultValue = "World") String content) {
        log.info("sayHello -> switchFlag: {}, input content: {}", switchFlag, content);

        return String.format("switchFlag: %s. Hello %s!", switchFlag, content);
    }

}
