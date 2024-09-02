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

    /**
     * Sends a new notification to a user.
     *
     * @param notification the notification object.
     * @return the saved notification.
     */
    public Notification sendNotification(Notification notification) {
        notification.setDate(new Date());
        notification.setReadStatus(false);
        return notificationRepository.save(notification);
    }

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return list of notifications.
     */
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    /**
     * Retrieves a specific notification by ID.
     *
     * @param id the ID of the notification.
     * @return the notification object if found, null otherwise.
     */
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    /**
     * Mark a notification as read.
     *
     * @param id the ID of the notification to mark as read.
     * @return the updated notification if found, null otherwise.
     */
    public Notification markNotificationAsRead(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setReadStatus(true);
            return notificationRepository.save(notification);
        }
        return null;
    }
}
