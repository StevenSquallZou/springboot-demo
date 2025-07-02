package demo.repository.world;


import demo.model.world.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    List<Country> findByName(String name);
}
