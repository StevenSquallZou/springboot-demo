package demo.service.impl;


import demo.model.world.Country;
import demo.repository.world.CountryRepository;
import demo.service.api.ICountryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service("countryServiceJpaImpl")
@Slf4j
public class CountryServiceJpaImpl implements ICountryService {
    protected CountryRepository countryRepository;


    @Autowired
    public CountryServiceJpaImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Country> queryByName(String name) {
        log.info("queryByName -> querying country repository by name: {}", name);
        List<Country> countryList = countryRepository.findByName(name);
        log.info("queryByName -> queried country count: {}", CollectionUtils.size(countryList));

        return countryList;
    }

}
