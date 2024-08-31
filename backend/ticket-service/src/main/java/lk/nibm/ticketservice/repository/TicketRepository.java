package lk.nibm.ticketservice.repository;

import lk.nibm.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface TicketRepository extends JpaRepository<Ticket,Integer> {
    @Query
    List<Ticket> findByUserId(int userId);
}
