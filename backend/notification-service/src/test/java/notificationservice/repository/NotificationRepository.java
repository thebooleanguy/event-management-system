package notificationservice.repository;public class NotificationRepository {
}
package lk.nibm.notificationservice.repository;

import lk.nibm.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
