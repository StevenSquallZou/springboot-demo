package demo.controller;


import demo.model.sakila.FilmText;
import demo.service.api.IFilmTextService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/filmText")
@Validated
@Slf4j
public class FilmTextController {
    protected IFilmTextService filmTextService;


    @GetMapping("/{filmId}")
    public FilmText getFilmText(@PathVariable Integer filmId) {
        log.info("getFilmText -> input filmId: {}", filmId);

        FilmText filmText = filmTextService.getFilmTextByFilmId(filmId);
        log.info("getFilmText -> output filmText: {}", filmText);

        return filmText;
    }


    @GetMapping
    public Object getFilmTextAttribute(
            @NotNull @RequestParam(name = "filmId") Integer filmId,
            @RequestParam(name = "attributeName") String attributeName
    ) {
        log.info("getFilmTextAttribute -> input filmId={}, attributeName={}", filmId, attributeName);

        Object attributeValue = filmTextService.getFilmTextAttribute(filmId, attributeName);
        log.info("getFilmTextAttribute -> attributeValue: {}", attributeValue);

        return attributeValue;
    }


    @PostMapping
    public int createFilmText(@Validated @RequestBody FilmText filmText) {
        log.info("saveFilmText -> input filmText: {}", filmText);

        int result = filmTextService.createFilmText(filmText);
        log.info("saveFilmText -> output result: {}", result);

        return result;
    }


    @Autowired
    @Qualifier("filmTextServiceRedisImpl")
    public void setFilmTextService(IFilmTextService filmTextService) {
        log.info("setFilmTextService -> Setting filmTextService: {}", filmTextService.getClass().getSimpleName());
        this.filmTextService = filmTextService;
    }

}
