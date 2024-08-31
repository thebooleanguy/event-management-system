package lk.nibm.eventservice.repository;

import lk.nibm.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface EventRepository extends JpaRepository<Event,Integer> {

    @Query("SELECT e FROM Event e WHERE e.title = :title")
    List<Event> findEventByTitle(@Param("title") String title);

}
