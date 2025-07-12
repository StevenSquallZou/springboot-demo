package demo.loader;


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
import static demo.config.RedisConfig.FILM_TEXT_HASH_KEY_PREFIX;
import static demo.config.RedisConfig.FILM_TEXT_VALUE_KEY_PREFIX;


@Component
@Slf4j
public class FilmTextRedisLoader implements CommandLineRunner {
    @Value("${app.redis.loadBatchSize}")
    protected int loadBatchSize;
    protected JdbcTemplate jdbcTemplate;
    protected RedisTemplate<String, Object> redisTemplate;


    @Autowired
    public FilmTextRedisLoader(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
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
                String key = FILM_TEXT_VALUE_KEY_PREFIX + filmText.getFilmId();
                redisTemplate.opsForValue().set(key, filmText);

                String hashKey = FILM_TEXT_HASH_KEY_PREFIX + filmText.getFilmId();
                redisTemplate.opsForHash().put(hashKey, "film_id", filmText.getFilmId());
                redisTemplate.opsForHash().put(hashKey, "title", filmText.getTitle());
                redisTemplate.opsForHash().put(hashKey, "description", filmText.getDescription());

                loadedCount++;
            }

            log.debug("batchLoadFilmText -> partially loaded {} rows from database", loadedCount);

            offset += loadedCount;
        }

        log.info("batchLoadFilmText -> totally loaded {} rows into Redis", offset);
    }

}
