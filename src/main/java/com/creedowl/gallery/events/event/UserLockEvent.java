package com.creedowl.gallery.events.event;

import com.creedowl.gallery.model.User;
import org.springframework.context.ApplicationEvent;

public class UserLockEvent extends ApplicationEvent {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public UserLockEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
