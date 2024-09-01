package lk.nibm.ticketservice.repository;

import lk.nibm.ticketservice.model.AvailableTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableTicketsRepository extends JpaRepository<AvailableTickets, Integer> {
    // Custom query methods can be added here if needed
}
