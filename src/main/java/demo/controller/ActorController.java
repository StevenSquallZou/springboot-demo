package demo.controller;


import demo.model.sakila.Actor;
import demo.service.api.IActorService;
import demo.dto.ActorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
    public Actor getActorByActorId(@PathVariable Integer actorId) {
        log.info("getActorByActorId -> input actorId: {}", actorId);

        return actorService.queryByActorId(actorId);
    }


    /**
     * Sample: <a href="http://localhost:8080/actor?name=NICK">...</a>
     * @param name - the actor name
     * @return the actor list
     */
    @GetMapping
    public List<Actor> getActorByName(@RequestParam(name = "name") String name) {
        log.info("getActorByName -> input name: {}", name);

        return actorService.queryByName(name);
    }


    /**
     * Sample: <a href="http://localhost:8080/actor">...</a>
     * @param actorDto - the actor dto
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<Actor> updateActor(@RequestBody ActorDto actorDto) {
        Actor actor = getActorByActorId(actorDto.getActorId());
        if (actor == null) {
            log.warn("updateActor -> actor not found for actorId: {}", actorDto.getActorId());
            return ResponseEntity.notFound().build();
        }

        actor.setFirstName(actorDto.getFirstName());
        actor.setLastName(actorDto.getLastName());

        Actor updatedActor = actorService.updateActor(actor);
        log.info("updateActor -> actor has been updated, updated actor: {}", updatedActor);

        return ResponseEntity.ok(updatedActor);
    }


    @Autowired
    @Qualifier("actorServiceJpaImpl")
    public void setActorService(IActorService actorService) {
        this.actorService = actorService;
    }

}
