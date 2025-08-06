package demo.controller;


import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/test")
@Validated
@Slf4j
public class SpringMVCController {

    @GetMapping
    public String test(@RequestParam(name = "message") @NotBlank String message, Model model) {
        log.info("test method called");

        model.addAttribute("message", message);

        // This will resolve to a view named "test.html" or "test.jsp" depending on your view resolver configuration
        return "test";
    }

}
