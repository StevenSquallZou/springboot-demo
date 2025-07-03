package demo.mapper;


import demo.model.sakila.FilmText;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;


@Mapper
public interface FilmTextMapper {
    @Select("SELECT * FROM sakila.film_text WHERE film_id = #{filmId}")
    FilmText selectByFilmId(Integer filmId);

    @Update("UPDATE sakila.film_text " +
            "SET title = #{title}, description = #{description} " +
            "WHERE film_id = #{filmId}")
    int updateFilmText(FilmText filmText);

    @Insert("INSERT INTO sakila.film_text (film_id, title, description) " +
            "VALUES (#{filmId}, #{title}, #{description})")
    int insertFilmText(FilmText filmText);

    @Delete("DELETE FROM sakila.film_text WHERE film_id = #{filmId}")
    int deleteFilmText(Integer filmId);
}
