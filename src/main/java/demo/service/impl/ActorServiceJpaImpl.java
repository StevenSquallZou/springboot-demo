package demo.service.impl;


import demo.model.sakila.Actor;
import demo.repository.sakila.ActorRepository;
import demo.service.api.IActorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service("actorServiceJpaImpl")
@Slf4j
public class ActorServiceJpaImpl implements IActorService {
    protected ActorRepository actorRepository;


    @Autowired
    public ActorServiceJpaImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public Actor queryByActorId(Integer actorId) {
        log.info("queryByName -> querying actor repository by actorId: {}", actorId);
        Optional<Actor> optional = actorRepository.findById(actorId);

        if (optional.isPresent()) {
            Actor actor = optional.get();
            log.info("queryByName -> actor found: {}", actor);
            return actor;
        } else {
            log.warn("queryByName -> actor not found for actorId: {}", actorId);
            return null;
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Actor> queryByName(String name) {
        log.info("queryByName -> querying actor repository by name: {}", name);
        List<Actor> actorList = actorRepository.findByName(name);
        log.info("queryByName -> queried actor count: {}", CollectionUtils.size(actorList));

        return actorList;
    }

}
