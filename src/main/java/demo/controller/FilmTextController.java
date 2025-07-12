package demo.controller;


import demo.model.sakila.FilmText;
import demo.service.api.IFilmTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/filmText")
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


    @PostMapping
    public int saveFilmText(@RequestBody FilmText filmText) {
        log.info("saveFilmText -> input filmText: {}", filmText);

        int result = filmTextService.saveFilmText(filmText);
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
