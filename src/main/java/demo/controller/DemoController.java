package demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class DemoController {

    /**
     * Sample: <a href="http://localhost:8080/hello?content=Steven">...</a>
     * @param content the content to include in the greeting, defaults to "World"
     * @return Hello {content}!
     */
    @GetMapping("/hello")
    public String sayHello(@RequestParam(name = "content", defaultValue = "World") String content) {
        log.info("sayHello -> input content: {}", content);

        return String.format("Hello %s!", content);
    }

}
