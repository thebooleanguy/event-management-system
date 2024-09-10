package lk.nibm.ticketservice.controller;

import lk.nibm.ticketservice.model.AvailableTickets;
import lk.nibm.ticketservice.model.Ticket;
import lk.nibm.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets/")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/all")
    public List<Ticket> findAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findTicketByID(@PathVariable int id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUser(@PathVariable int userId) {
        List<Ticket> tickets = ticketService.getTicketsByUser(userId);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable int id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/book")
    public ResponseEntity<Ticket> bookTicket(@RequestBody Ticket ticketRequest) {
        try {
            Ticket newTicket = ticketService.bookTicket(ticketRequest);
            return ResponseEntity.ok(newTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
