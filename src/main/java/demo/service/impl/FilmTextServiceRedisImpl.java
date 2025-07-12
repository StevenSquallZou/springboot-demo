package demo.service.impl;


import demo.config.RedisConfig;
import demo.model.sakila.FilmText;
import demo.service.api.IFilmTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service("filmTextServiceRedisImpl")
@Slf4j
public class FilmTextServiceRedisImpl implements IFilmTextService {
    protected RedisTemplate<String, Object> redisTemplate;


    public FilmTextServiceRedisImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public FilmText getFilmTextByFilmId(Integer filmId) {
        String key = RedisConfig.FILM_TEXT_VALUE_KEY_PREFIX + filmId;
        log.info("getFilmTextByFilmId -> retrieving film text for filmId: {}, redis key: {}", filmId, key);

        return (FilmText) redisTemplate.opsForValue().get(key);
    }


    @Override
    public int saveFilmText(FilmText filmText) {
        return 0;
    }
}
