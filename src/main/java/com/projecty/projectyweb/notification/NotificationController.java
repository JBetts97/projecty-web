package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notifications")
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotificationsForUser(userService.getCurrentUser());
    }

    @GetMapping("getUnreadNotificationCount")
    public long getUnreadNotificationCount() {
        return notificationService.getUnreadNotificationCountForSpecifiedUser(userService.getCurrentUser());
    }
}
