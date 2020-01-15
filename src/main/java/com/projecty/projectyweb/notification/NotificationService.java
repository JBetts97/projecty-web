package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.project.Project;
import com.projecty.projectyweb.project.ProjectRepository;
import com.projecty.projectyweb.user.User;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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

    //    TODO Untested
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

}
