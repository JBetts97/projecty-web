package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);

    List<Notification> findAllByUserAndSeen(User user, boolean seen);
    Long countByUserAndSeen(User user, boolean seen);
}
