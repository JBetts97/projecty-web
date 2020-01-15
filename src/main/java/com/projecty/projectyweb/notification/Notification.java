package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    private User user;

    private Notifications type;

    private Long[] objectIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notifications getType() {
        return type;
    }

    public void setType(Notifications type) {
        this.type = type;
    }

    public Long[] getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(Long[] objectIds) {
        this.objectIds = objectIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", objectIds=" + Arrays.toString(objectIds) +
                '}';
    }
}
