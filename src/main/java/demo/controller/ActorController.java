package demo.controller;


import demo.model.sakila.Actor;
import demo.service.api.IActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/actor")
@Slf4j
public class ActorController {
    protected IActorService actorService;


    /**
     * Sample: <a href="http://localhost:8080/actor/1">...</a>
     * @param actorId - the actor id
     * @return the actor
     */
    @GetMapping("/{actorId}")
    public Actor queryByActorId(@PathVariable Integer actorId) {
        log.info("queryByActorId -> input actorId: {}", actorId);

        return actorService.queryByActorId(actorId);
    }


    /**
     * Sample: <a href="http://localhost:8080/actor?name=NICK">...</a>
     * @param name - the actor name
     * @return the actor list
     */
    @GetMapping
    public List<Actor> queryActorByName(@RequestParam(name = "name") String name) {
        log.info("queryActorByName -> input name: {}", name);

        return actorService.queryByName(name);
    }


    @Autowired
    @Qualifier("actorServiceJpaImpl")
    public void setActorService(IActorService actorService) {
        this.actorService = actorService;
    }

}
