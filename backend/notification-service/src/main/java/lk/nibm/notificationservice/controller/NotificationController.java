package lk.nibm.notificationservice.controller;

import lk.nibm.notificationservice.model.Notification;
import lk.nibm.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing notifications.
 */
@RestController
@RequestMapping("/api/notifications/")  // Base path for the Notification API
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // -----------------------------------------------------------
    // Standard JPA Methods (CRUD)
    // -----------------------------------------------------------

    /**
     * Endpoint to create (send) a new notification.
     * Standard CRUD Create operation.
     *
     * @param notification the notification object.
     * @return the saved notification.
     */
    @PostMapping("/send")
    public Notification sendNotification(@RequestBody Notification notification) {
        return notificationService.sendNotification(notification);
    }

    /**
     * Endpoint to retrieve a specific notification by ID.
     * Standard CRUD Read operation.
     *
     * @param id the ID of the notification.
     * @return the notification object if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    public Optional<Notification> getNotificationById(@PathVariable Long id) {
        return Optional.ofNullable(notificationService.getNotificationById(id));
    }

    /**
     * Endpoint to retrieve all notifications.
     * Standard CRUD Read operation (find all).
     *
     * @return list of all notifications.
     */
    @GetMapping("/all")
    public List<Notification> findAllNotifications() {
        return notificationService.findAllNotifications();
    }

    /**
     * Endpoint to update an existing notification.
     * Standard CRUD Update operation.
     *
     * @param id the ID of the notification to update.
     * @param notification the updated notification object.
     * @return the updated notification if found, 404 otherwise.
     */
    @PutMapping("/{id}")
    public Optional<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        return Optional.ofNullable(notificationService.updateNotification(id, notification));
    }

    /**
     * Endpoint to delete a notification by ID.
     * Standard CRUD Delete operation.
     *
     * @param id the ID of the notification to delete.
     * @return 200 OK if deleted, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public boolean deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }

    // -----------------------------------------------------------
    // Custom JPA Methods (Additional Operations)
    // -----------------------------------------------------------

    /**
     * Endpoint to retrieve all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return list of notifications for the user.
     */
    @GetMapping("/user")
    public List<Notification> getNotificationsForUser(@RequestParam Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    /**
     * Endpoint to mark a notification as read.
     *
     * @param id the ID of the notification to mark as read.
     * @return the updated notification if found, 404 otherwise.
     */
    @PatchMapping("/{id}/read")
    public Optional<Notification> markNotificationAsRead(@PathVariable Long id) {
        return Optional.ofNullable(notificationService.markNotificationAsRead(id));
    }

    /**
     * Endpoint to mark a specific notification as unread.
     *
     * @param id the ID of the notification to mark as unread.
     * @return the updated notification if found, 404 otherwise.
     */
    @PatchMapping("/{id}/unread")
    public Optional<Notification> markNotificationAsUnread(@PathVariable Long id) {
        return Optional.ofNullable(notificationService.markNotificationAsUnread(id));
    }

    /**
     * Endpoint to delete all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return 200 OK response if deletion is successful.
     */
    @DeleteMapping("/user/{userId}/deleteAll")
    public void deleteAllNotificationsForUser(@PathVariable Long userId) {
        notificationService.deleteAllNotificationsForUser(userId);
    }
}
