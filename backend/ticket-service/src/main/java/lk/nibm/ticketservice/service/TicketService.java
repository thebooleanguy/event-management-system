package lk.nibm.ticketservice.service;

import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTickets(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(int id){

        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByUser(int userId) {
        return ticketRepository.findByUserId(userId);
    }

    public void cancelTicket( int id) {
        ticketRepository.deleteById(id);
    }
}
