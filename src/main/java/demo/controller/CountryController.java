package demo.controller;


import demo.model.world.Country;
import demo.service.api.ICountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@Tag(name = "Country Management", description = "Operations to manage countries")
@RestController
@Validated
@Slf4j
public class CountryController {
    protected ICountryService countryService;


    /**
     * Sample: <a href="http://localhost:8080/country?name=China">...</a>
     * @param name - the country name
     * @return the country list
     */
    @Operation(
        summary = "Query Countries",
        description = "Query a list of countries with the given name",
        parameters = {
            @Parameter(name = "name", description = "name of the country to fetch", required = true, example = "China", in = ParameterIn.QUERY)
        }
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved data", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Country.class, description = "the fetched country list"))))
    @ApiResponse(responseCode = "400", description = "Invalid name supplied")
    @GetMapping("/country")
    public List<Country> getCountryByName(@RequestParam(name = "name") @NotBlank @Size(min = 1, max = 52) String name) {
        log.info("getCountryByName -> input name: {}", name);

        return countryService.queryByName(name);
    }


    @Autowired
    @Qualifier("countryServiceJpaImpl")
    public void setCountryService(ICountryService countryService) {
        this.countryService = countryService;
    }

}
