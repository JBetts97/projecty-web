package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.project.Project;
import com.projecty.projectyweb.project.ProjectRepository;
import com.projecty.projectyweb.user.User;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final MessageSource messageSource;
    private final ProjectRepository projectRepository;

    public NotificationService(NotificationRepository notificationRepository, MessageSource messageSource, ProjectRepository projectRepository) {
        this.notificationRepository = notificationRepository;
        this.messageSource = messageSource;
        this.projectRepository = projectRepository;
    }

    public Notification createNotificationAndSave(User to, Notifications type, Long[] objectIds) {
        Notification notification = new NotificationBuilder()
                .setUser(to)
                .setType(type)
                .setObjectIds(objectIds)
                .createNotification();
        return notificationRepository.save(notification);
    }

    public String getNotificationString(Notification notification) {
        String[] values = createStringArgumentValuesArray(notification.getType(), notification.getObjectIds());
        return getMessageFromMessageSource(notification.getType(), values);
    }

    public String[] createStringArgumentValuesArray(Notifications type, Long[] objectIds) {
        switch (type) {
            case USER_ADDED_TO_PROJECT:
                Optional<Project> optionalProject = projectRepository.findById(objectIds[0]);
                if (optionalProject.isPresent())
                    return new String[]{optionalProject.get().getName()};
                break;
        }
        return null;
    }

    public String getMessageFromMessageSource(Notifications type, String[] values) {
        return messageSource.getMessage(convertTypeToLowerCaseString(type), values, Locale.getDefault());
    }

    private String convertTypeToLowerCaseString(Notifications type) {
        return String.valueOf(type).toLowerCase();
    }

    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    private List<Notification> findAllByUser(User user) {
        return notificationRepository.findAllByUser(user);
    }

    public List<Notification> getAllNotificationsForUser(User user) {
        List<Notification> notifications = findAllByUser(user);
        notifications.forEach(notification -> notification.setMessage(getNotificationString(notification)));
        notifications.forEach(notification -> System.out.println(Arrays.toString(notification.getObjectIds())));
        return notifications;
    }

    public long getUnreadNotificationCountForSpecifiedUser(User user) {
        return notificationRepository.countByUserAndSeen(user, false);
    }
}
