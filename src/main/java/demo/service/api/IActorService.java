package demo.service.api;


import demo.model.sakila.Actor;
import java.util.List;


public interface IActorService {
    Actor queryByActorId(Integer actorId);

    List<Actor> queryByName(String name);
}
