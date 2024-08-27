package lk.nibm.eventservice.repository;

import lk.nibm.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<Event,Integer> {

    @Query("select e from Event where e.name")
    List <Event> findEventByName (String name);

}
