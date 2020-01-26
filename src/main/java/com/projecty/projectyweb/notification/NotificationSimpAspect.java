package com.projecty.projectyweb.notification;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotificationSimpAspect {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public NotificationSimpAspect(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @AfterReturning(pointcut = "execution(* com.projecty.projectyweb.notification.NotificationService.createNotificationAndSave(..)))", returning = "returnedValue")
    public void sendNotificationToUser(Object returnedValue) {
        if (returnedValue instanceof Notification) {
            Notification notification = (Notification) returnedValue;
            notification.setMessage(
                    notificationService.getNotificationString(notification)
            );
            messagingTemplate.convertAndSendToUser(
                    notification.getUser().getUsername(), "/queue/notifications", notification);
        }
    }
}
