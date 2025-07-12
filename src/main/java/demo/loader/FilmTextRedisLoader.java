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


@Component
@Slf4j
public class FilmTextRedisLoader implements CommandLineRunner {
    public static final String FILM_TEXT_KEY_PREFIX = "filmText:";
    public static final String FILM_TEXT_HASH_KEY_PREFIX = "film_text:";

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
    public void run(String... args) throws Exception {
        batchLoadFilmText(loadBatchSize);
    }


    public void batchLoadFilmText(int loadBatchSize) {
        log.info("batchLoadFilmText -> Starting batch load of film text data into Redis...");

        boolean hasMore = true;
        int offset = 0;

        while (hasMore) {
            String sql = String.format("SELECT * FROM sakila.film_text ORDER BY FILM_ID LIMIT %d OFFSET %d", loadBatchSize, offset);
            List<FilmText> filmTextList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(FilmText.class));

            if (CollectionUtils.isEmpty(filmTextList)) {
                break;
            } else if (filmTextList.size() < loadBatchSize) {
                hasMore = false;
            }

            for (FilmText filmText : filmTextList) {
                String key = FILM_TEXT_KEY_PREFIX + filmText.getFilmId();
                redisTemplate.opsForValue().set(key, filmText);

                String hashKey = FILM_TEXT_HASH_KEY_PREFIX + filmText.getFilmId();
                redisTemplate.opsForHash().put(hashKey, "film_id", filmText.getFilmId());
                redisTemplate.opsForHash().put(hashKey, "title", filmText.getTitle());
                redisTemplate.opsForHash().put(hashKey, "description", filmText.getDescription());
            }

            offset += filmTextList.size();
        }

        log.info("batchLoadFilmText -> loaded {} rows into Redis", offset);
    }

}
