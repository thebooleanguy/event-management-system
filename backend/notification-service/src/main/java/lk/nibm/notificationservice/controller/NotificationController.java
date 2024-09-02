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
@RequestMapping("/api/notifications")  // Updated base path with '/api'
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Endpoint to send a new notification.
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
     * Endpoint to retrieve all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return list of notifications.
     */
    @GetMapping("/user")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Endpoint to retrieve a specific notification by ID.
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
}
