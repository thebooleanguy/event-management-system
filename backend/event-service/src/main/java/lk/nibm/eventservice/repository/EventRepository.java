package lk.nibm.eventservice.repository;

import lk.nibm.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<Event,Integer> {


    List <Event> findEventByTitle(String title);

}
