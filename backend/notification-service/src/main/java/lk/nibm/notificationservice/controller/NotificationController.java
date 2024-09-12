package lk.nibm.notificationservice.controller;

import lk.nibm.notificationservice.model.Notification;
import lk.nibm.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.sendNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    /**
     * Endpoint to retrieve a specific notification by ID.
     * Standard CRUD Read operation.
     *
     * @param id the ID of the notification.
     * @return the notification object if found, 404 otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to retrieve all notifications.
     * Standard CRUD Read operation (find all).
     *
     * @return list of all notifications.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Notification>> findAllNotifications() {
        List<Notification> notifications = notificationService.findAllNotifications();
        return ResponseEntity.ok(notifications);
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
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.updateNotification(id, notification);
        if (updatedNotification != null) {
            return ResponseEntity.ok(updatedNotification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to delete a notification by ID.
     * Standard CRUD Delete operation.
     *
     * @param id the ID of the notification to delete.
     * @return 200 OK if deleted, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        boolean deleted = notificationService.deleteNotification(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<List<Notification>> getNotificationsForUser(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Endpoint to mark a notification as read.
     *
     * @param id the ID of the notification to mark as read.
     * @return the updated notification if found, 404 otherwise.
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long id) {
        Notification updatedNotification = notificationService.markNotificationAsRead(id);
        if (updatedNotification != null) {
            return ResponseEntity.ok(updatedNotification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to mark a specific notification as unread.
     *
     * @param id the ID of the notification to mark as unread.
     * @return the updated notification if found, 404 otherwise.
     */
    @PatchMapping("/{id}/unread")
    public ResponseEntity<Notification> markNotificationAsUnread(@PathVariable Long id) {
        Notification updatedNotification = notificationService.markNotificationAsUnread(id);
        if (updatedNotification != null) {
            return ResponseEntity.ok(updatedNotification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to delete all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return 200 OK response if deletion is successful.
     */
    @DeleteMapping("/user/{userId}/deleteAll")
    public ResponseEntity<Void> deleteAllNotificationsForUser(@PathVariable Long userId) {
        notificationService.deleteAllNotificationsForUser(userId);
        return ResponseEntity.ok().build();
    }
}
