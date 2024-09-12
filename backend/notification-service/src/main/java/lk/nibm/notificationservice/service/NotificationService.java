package lk.nibm.notificationservice.service;

import lk.nibm.notificationservice.model.Notification;
import lk.nibm.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling notification-related operations.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ===================== Standard JPA Methods =====================

    /**
     * Sends (create) a new notification to a user.
     * Standard CRUD Create operation.
     *
     * @param notification the notification object.
     * @return the saved notification.
     */
    public Notification sendNotification(Notification notification) {
        return notificationRepository.save(notification); // Saves the new notification
    }

    /**
     * Retrieves all notifications from the repository.
     * Standard CRUD Read operation.
     *
     * @return list of all notifications.
     */
    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll(); // Fetches all notifications from the database
    }

    /**
     * Retrieves a specific notification by ID.
     * Standard CRUD Read operation.
     *
     * @param id the ID of the notification.
     * @return the notification object if found, null otherwise.
     */
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null); // Fetches by ID or returns null if not found
    }

    /**
     * Deletes a notification by ID.
     * Standard CRUD Delete operation.
     *
     * @param id the ID of the notification to be deleted.
     * @return true if deleted successfully, false if not found.
     */
    public boolean deleteNotification(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            notificationRepository.deleteById(id); // Deletes notification if found
            return true;
        }
        return false;
    }

    /**
     * Updates an existing notification.
     * Standard CRUD Update operation.
     *
     * @param id the ID of the notification to be updated.
     * @param updatedNotification the updated notification object.
     * @return the updated notification if found, null otherwise.
     */
    public Notification updateNotification(Long id, Notification updatedNotification) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setContent(updatedNotification.getContent());
            notification.setReadStatus(updatedNotification.getReadStatus()); // Updates fields
            return notificationRepository.save(notification); // Saves the updated notification
        }
        return null;
    }

    // ===================== Custom JPA Methods =====================

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return list of notifications.
     */
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId); // Fetches notifications for the given user
    }

    /**
     * Marks a notification as read.
     *
     * @param id the ID of the notification to mark as read.
     * @return the updated notification if found, null otherwise.
     */
    public Notification markNotificationAsRead(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setReadStatus(true); // Marks the notification as read
            return notificationRepository.save(notification);
        }
        return null;
    }

    /**
     * Deletes all notifications for a specific user.
     *
     * @param userId the ID of the user.
     */
    public void deleteAllNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notificationRepository.deleteAll(notifications); // Deletes all notifications for the user
    }

    /**
     * Marks a notification as unread.
     *
     * @param id the ID of the notification to mark as unread.
     * @return the updated notification if found, null otherwise.
     */
    public Notification markNotificationAsUnread(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setReadStatus(false); // Marks the notification as unread
            return notificationRepository.save(notification);
        }
        return null;
    }
}
