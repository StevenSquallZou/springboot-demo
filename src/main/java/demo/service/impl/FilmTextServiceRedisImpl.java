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
        FilmText filmText = filmTextRedisManager.getFilmText(filmId);
        if (filmText != null) {
            log.info("getFilmTextByFilmId -> Film text found in Redis for filmId={}", filmId);
            return filmText;
        }

        filmText = filmTextRedisManager.reloadFromDB(filmId);
        if (filmText != null) {
            return filmText;
        }

        log.warn("getFilmTextByFilmId -> No film text found in Redis and DB for filmId={}", filmId);

        return null;
    }


    @Override
    @Transactional
    public int createFilmText(FilmText filmText) {
        log.info("saveFilmText -> inserting film text to DB: {}", filmText);
        int result = filmTextMapper.insertFilmText(filmText);
        log.info("saveFilmText -> insert result: {}", result);

        filmTextRedisManager.updateAll(filmText);
        log.info("saveFilmText -> updated redis for filmId={}", filmText.getFilmId());

        return result;
    }


    @Override
    public Object getFilmTextAttribute(Integer filmId, String attributeName) {
        Object filmTextAttributeValue = filmTextRedisManager.getFilmTextAttribute(filmId, attributeName);
        if (filmTextAttributeValue != null) {
            log.info("getFilmTextAttribute -> Film text attribute found in Redis for filmId={}, filmTextAttributeValue={}", filmId, filmTextAttributeValue);
            return filmTextAttributeValue;
        }

        FilmText filmText = filmTextRedisManager.reloadFromDB(filmId);
        if (filmText != null) {
            filmTextAttributeValue = filmTextRedisManager.getFilmTextAttribute(filmId, attributeName);
            return filmTextAttributeValue;
        }

        log.warn("getFilmTextAttribute -> No film text found in Redis and DB for filmId={}", filmId);

        return null;
    }

}
