package demo.repository.sakila;


import demo.model.sakila.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    @Query(value = "select a from Actor a where firstName like %:name% or lastName like %:name%")
    List<Actor> findByName(@Param("name") String name);
}
