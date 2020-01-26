package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.user.UserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(params = "status")
    public List<Notification> getUnreadOrReadNotifications(@RequestParam("status") NotificationStatus status) {
        return notificationService.getNotificationWithNotificationStatus(userService.getCurrentUser(), status);
    }

    @GetMapping("getUnreadNotificationCount")
    public long getUnreadNotificationCount() {
        return notificationService.getUnreadNotificationCountForSpecifiedUser(userService.getCurrentUser());
    }

    @PostMapping("setAllRead")
    public void readAllNotifications() {
        notificationService.setAllNotificationsReadForSpecifiedUser(userService.getCurrentUser());
    }
}