package lk.nibm.eventservice.model;

import jakarta.persistence.*;

@Entity
@Table(name ="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private  int id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name=" date")
    private String date;

    @Column(name="location")
    private String location;

    @Column(name="organizerId")
    private  int organizerId;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    public  Event() {

    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public enum EventCategory {
        MUSIC,
        THEATER,
        CONCERT,
        SPORT,
        CONFERENCE,
        OTHER
    }

}
