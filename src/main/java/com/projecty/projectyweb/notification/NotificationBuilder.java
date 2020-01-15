package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.user.User;

public class NotificationBuilder {
    private User user;
    private Notifications type;
    private Long[] objectIds;

    public NotificationBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public NotificationBuilder setType(Notifications type) {
        this.type = type;
        return this;
    }

    public NotificationBuilder setObjectIds(Long[] objectIds) {
        this.objectIds = objectIds;
        return this;
    }

    public Notification createNotification() {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setObjectIds(objectIds);
        return notification;
    }
}