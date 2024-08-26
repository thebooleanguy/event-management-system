package lk.nibm.notificationservice.repository;

import lk.nibm.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds all notifications for a specific user.
     *
     * @param userId the ID of the user.
     * @return list of notifications.
     */
    List<Notification> findByUserId(Long userId);
}
