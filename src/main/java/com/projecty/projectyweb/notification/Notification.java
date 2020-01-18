package com.projecty.projectyweb.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projecty.projectyweb.user.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private User user;

    private Notifications type;

    private Long[] objectIds;

    @CreationTimestamp
    private Date date;

    @Transient
    private String message;

    private boolean seen;

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

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(getId(), that.getId()) &&
                getType() == that.getType() &&
                Arrays.equals(getObjectIds(), that.getObjectIds());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getType());
        result = 31 * result + Arrays.hashCode(getObjectIds());
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", objectIds=" + Arrays.toString(objectIds) +
                ", date=" + date +
                ", message='" + message + '\'' +
                ", seen=" + seen +
                '}';
    }
}
