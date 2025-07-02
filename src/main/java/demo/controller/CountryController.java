package demo.controller;


import demo.model.world.Country;
import demo.service.api.ICountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@Slf4j
public class CountryController {
    protected ICountryService countryService;


    /**
     * Sample: <a href="http://localhost:8080/country?name=China">...</a>
     * @param name - the country name
     * @return the country list
     */
    @GetMapping("/country")
    public List<Country> queryCountryByName(@RequestParam(name = "name") String name) {
        log.info("queryCountryByName -> input name: {}", name);

        return countryService.queryByName(name);
    }


    @Autowired
    @Qualifier("countryServiceJpaImpl")
    public void setCountryService(ICountryService countryService) {
        this.countryService = countryService;
    }

}
