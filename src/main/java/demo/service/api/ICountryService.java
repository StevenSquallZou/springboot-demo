package demo.service.api;


import demo.model.world.Country;
import java.util.List;


public interface ICountryService {
    List<Country> queryByName(String name);
}
