package demo.service.impl;


import demo.mapper.FilmTextMapper;
import demo.model.sakila.FilmText;
import demo.redis.FilmTextRedisManager;
import demo.service.api.IFilmTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("filmTextServiceRedisImpl")
@Slf4j
public class FilmTextServiceRedisImpl implements IFilmTextService {
    protected FilmTextRedisManager filmTextRedisManager;
    protected FilmTextMapper filmTextMapper;


    @Autowired
    public FilmTextServiceRedisImpl(FilmTextRedisManager filmTextRedisManager, FilmTextMapper filmTextMapper) {
        this.filmTextRedisManager = filmTextRedisManager;
        this.filmTextMapper = filmTextMapper;
    }


    @Override
    public FilmText getFilmTextByFilmId(Integer filmId) {
        return filmTextRedisManager.getFilmText(filmId);
    }


    @Override
    @Transactional
    public int createFilmText(FilmText filmText) {
        log.info("saveFilmText -> inserting film text to DB: {}", filmText);
        int result = filmTextMapper.insertFilmText(filmText);
        log.info("saveFilmText -> insert result: {}", result);

        filmTextRedisManager.update(filmText);
        log.info("saveFilmText -> updated redis for filmId={}", filmText.getFilmId());

        return result;
    }


    @Override
    public Object getFilmTextAttribute(Integer filmId, String attributeName) {
        return filmTextRedisManager.getFilmTextAttribute(filmId, attributeName);
    }

}
