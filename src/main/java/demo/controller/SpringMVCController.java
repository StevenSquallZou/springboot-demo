package demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/test")
@Slf4j
public class SpringMVCController {

    @GetMapping
    public String test(Model model) {
        log.info("test method called");

        model.addAttribute("message", "This is a test message for Spring MVC");

        // This will resolve to a view named "test.html" or "test.jsp" depending on your view resolver configuration
        return "test";
    }

}
