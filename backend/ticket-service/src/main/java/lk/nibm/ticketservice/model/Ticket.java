package lk.nibm.ticketservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name=" Id")
    private int  Id;

    @Column(name="eventId")
    private int  eventId;

    @Column(name="paymentId")
    private int  paymentId;

    @Column(name="eventName")
    private String eventName;

    @Column(name="userId")
    private  int userId;

    @Column(name="venueName")
    private String venueName;

    @Column(name="seatNumber")
    private int seatNumber;

    @Column(name="totalTicket")
    private int totalTicket;

    @Column(name="totalPrice")
    private double totalPrice;

    @Column(name="bookingDate")
    private  String bookingDate;

    @Column(name="eventTime")
    private  String eventTime;

    public Ticket(){

    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public  String getEventTime() {
        return eventTime;
    }

    public void setEventTime( String eventTime) {
        this.eventTime = eventTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalTicket() {
        return totalTicket;
    }

    public void setTotalTicket(int totalTicket) {
        this.totalTicket = totalTicket;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
}
