package demo.redis;


import demo.model.sakila.FilmText;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@Slf4j
public class FilmTextRedisManager implements CommandLineRunner {
    public static final String FILM_TEXT_VALUE_KEY_PREFIX = "filmTextValue:";
    public static final String FILM_TEXT_HASH_KEY_PREFIX = "filmTextHash:";

    @Value("${app.redis.loadBatchSize}")
    protected int loadBatchSize;
    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public FilmTextRedisManager(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void run(String... args) {
        batchLoadFilmText(loadBatchSize);
    }


    public void batchLoadFilmText(int loadBatchSize) {
        log.info("batchLoadFilmText -> Starting batch load of film text data into Redis, loadBatchSize: {}", loadBatchSize);

        boolean hasMore = true;
        int offset = 0;

        while (hasMore) {
            String sql = String.format("SELECT * FROM sakila.film_text ORDER BY FILM_ID LIMIT %d OFFSET %d", loadBatchSize, offset);
            log.debug("batchLoadFilmText -> Executing SQL: {}", sql);
            List<FilmText> filmTextList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FilmText.class));

            if (CollectionUtils.isEmpty(filmTextList)) {
                log.debug("batchLoadFilmText -> No more film text data to load, exiting loop");
                break;
            } else if (filmTextList.size() < loadBatchSize) {
                log.info("batchLoadFilmText -> Loaded {} rows, which is less than loadBatchSize, indicating no more data", filmTextList.size());
                hasMore = false;
            }

            log.debug("batchLoadFilmText -> queried {} rows from database", filmTextList.size());

            int loadedCount = 0;
            for (FilmText filmText : filmTextList) {
                updateOpsForValue(filmText);

                updateOpsForHash(filmText);

                loadedCount++;
            }

            log.debug("batchLoadFilmText -> partially loaded {} rows from database", loadedCount);

            offset += loadedCount;
        }

        log.info("batchLoadFilmText -> totally loaded {} rows into Redis", offset);
    }


    public FilmText getFilmText(Integer filmId) {
        String valueKey = getFilmTextValueKey(filmId);
        log.info("getFilmTextByFilmId -> retrieving film text for filmId: {}, redis valueKey: {}", filmId, valueKey);

        return (FilmText) redisTemplate.opsForValue().get(valueKey);
    }


    public Object getFilmTextAttribute(Integer filmId, String attributeName) {
        String hashKey = getFilmTextHashKey(filmId);

        return redisTemplate.opsForHash().get(hashKey, attributeName);
    }


    public void update(FilmText filmText) {
        updateOpsForValue(filmText);

        updateOpsForHash(filmText);
    }


    public void updateOpsForValue(FilmText filmText) {
        String valueKey = getFilmTextValueKey(filmText.getFilmId());
        redisTemplate.opsForValue().set(valueKey, filmText);
    }


    private void updateOpsForHash(FilmText filmText) {
        String hashKey = getFilmTextHashKey(filmText.getFilmId());
        redisTemplate.opsForHash().put(hashKey, "film_id", filmText.getFilmId());
        redisTemplate.opsForHash().put(hashKey, "title", filmText.getTitle());
        redisTemplate.opsForHash().put(hashKey, "description", filmText.getDescription());
    }


    public static String getFilmTextValueKey(Integer filmId) {
        return FILM_TEXT_VALUE_KEY_PREFIX + filmId;
    }


    public static String getFilmTextHashKey(Integer filmId) {
        return FILM_TEXT_HASH_KEY_PREFIX + filmId;
    }

}
